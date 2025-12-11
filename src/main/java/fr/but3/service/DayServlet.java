package fr.but3.service;

import fr.but3.model.Slot;
import fr.but3.utils.Config;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/day")
public class DayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            res.sendRedirect(req.getContextPath() + "/calendar");
            return;
        }

        String mode = Optional.ofNullable(req.getParameter("mode"))
                .orElse("creneaux");

        LocalDate date = LocalDate.parse(dateParam);
        LocalDate today = LocalDate.now();
        int maxDelay = Config.getMaxReservationDays();

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Slot> slots = em.createQuery(
                    "SELECT s FROM Slot s WHERE s.date = :d ORDER BY s.startTime",
                    Slot.class
            )
            .setParameter("d", date)
            .getResultList();

            List<Object[]> usedRows = em.createQuery(
                    "SELECT r.slot.id, SUM(r.nbPersonnes) " +
                    "FROM Reservation r " +
                    "WHERE r.slot.date = :d " +
                    "GROUP BY r.slot.id",
                    Object[].class
            )
            .setParameter("d", date)
            .getResultList();

            Map<Integer, Integer> usedBySlotId = new HashMap<>();
            for (Object[] row : usedRows) {
                Integer sid = (Integer) row[0];
                Long used = (Long) row[1];
                usedBySlotId.put(sid, used.intValue());
            }

            Set<Integer> joursActifs = Config.getEnabledDays();
            boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());

            Set<LocalDate> holidays = Config.getHolidays(date.getYear());
            boolean ferie = holidays.contains(date);

            boolean limiteDepassee =
                    date.isAfter(today.plusDays(maxDelay)) ||
                    date.isBefore(today);

            List<Map<String, Object>> creneaux = new ArrayList<>();

            for (Slot s : slots) {
                int used = usedBySlotId.getOrDefault(s.getId(), 0);
                boolean complet = used >= s.getCapacity();

                boolean reservable =
                        !complet &&
                        !limiteDepassee &&
                        ouvert &&
                        !ferie;

                Map<String, Object> m = new HashMap<>();
                m.put("slot", s);
                m.put("used", used);
                m.put("complet", complet);
                m.put("reservable", reservable);

                creneaux.add(m);
            }

            Map<Integer, List<Map<String, Object>>> hours = new LinkedHashMap<>();
            for (int h = 0; h < 24; h++) {
                hours.put(h, new ArrayList<>());
            }

            for (Map<String, Object> m : creneaux) {
                Slot s = (Slot) m.get("slot");
                int hour = s.getStartTime().getHour();
                hours.get(hour).add(m);
            }

            req.setAttribute("creneaux", creneaux);
            req.setAttribute("hours", hours);
            req.setAttribute("mode", mode);
            req.setAttribute("date", date);

            req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
            req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

            req.getRequestDispatcher("/WEB-INF/views/day.jsp")
                    .forward(req, res);

        } finally {
            em.close();
        }
    }
}