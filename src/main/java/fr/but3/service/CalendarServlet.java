package fr.but3.service;

import fr.but3.model.JourStats;
import fr.but3.model.Slot;
import fr.but3.utils.Config;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDate today = LocalDate.now();

            String m = req.getParameter("month");
            String y = req.getParameter("year");

            int month = (m == null) ? today.getMonthValue() : Integer.parseInt(m);
            int year  = (y == null) ? today.getYear()        : Integer.parseInt(y);

            YearMonth ym = YearMonth.of(year, month);

            Set<Integer> joursActifs = Config.getEnabledDays();
            Set<LocalDate> joursFeries = Config.getHolidays(year);

            int maxDelay = Config.getMaxReservationDays();

            List<Slot> allSlots = em.createQuery(
                    "SELECT s FROM Slot s " +
                    "WHERE FUNCTION('YEAR', s.date) = :y " +
                    "AND FUNCTION('MONTH', s.date) = :m",
                    Slot.class
            )
            .setParameter("y", year)
            .setParameter("m", month)
            .getResultList();

            Map<LocalDate, List<Slot>> slotsByDate = new HashMap<>();
            for (Slot s : allSlots) {
                slotsByDate
                        .computeIfAbsent(s.getDate(), d -> new ArrayList<>())
                        .add(s);
            }

            List<Object[]> usedRows = em.createQuery(
                    "SELECT r.slot.id, SUM(r.nbPersonnes) " +
                    "FROM Reservation r " +
                    "WHERE FUNCTION('YEAR', r.slot.date) = :y " +
                    "AND FUNCTION('MONTH', r.slot.date) = :m " +
                    "GROUP BY r.slot.id",
                    Object[].class
            )
            .setParameter("y", year)
            .setParameter("m", month)
            .getResultList();

            Map<Integer, Integer> usedBySlotId = new HashMap<>();
            for (Object[] row : usedRows) {
                Integer sid = (Integer) row[0];
                Long used = (Long) row[1];
                usedBySlotId.put(sid, used.intValue());
            }

            Map<Integer, JourStats> stats = new HashMap<>();

            for (int d = 1; d <= ym.lengthOfMonth(); d++) {

                LocalDate date = LocalDate.of(year, month, d);

                boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());
                boolean ferie  = joursFeries.contains(date);

                boolean limiteDepassee =
                        date.isAfter(today.plusDays(maxDelay)) ||
                        date.isBefore(today);

                List<Slot> slots = slotsByDate.getOrDefault(date, Collections.emptyList());

                int dispo = 0;
                int totalPers = 0;
                int totalCap = 0;

                for (Slot s : slots) {
                    int used = usedBySlotId.getOrDefault(s.getId(), 0);
                    totalPers += used;
                    totalCap  += s.getCapacity();

                    if (used < s.getCapacity()) {
                        dispo++;
                    }
                }

                double taux;
                if (totalCap == 0) {
                    taux = 0.0;
                } else {
                    taux = (double) totalPers / totalCap;
                    if (taux < 0) taux = 0;
                    if (taux > 1) taux = 1;
                }

                stats.put(d, new JourStats(
                        dispo,
                        totalPers,
                        ouvert,
                        ferie,
                        taux,
                        limiteDepassee
                ));
            }

            int firstDayIndex = rotationIndex(Config.get("planning.premier_jour_semaine"));
            int firstDayMonth = rotateDay(
                    ym.atDay(1).getDayOfWeek().getValue(),
                    firstDayIndex
            );

            req.setAttribute("stats", stats);
            req.setAttribute("year", year);
            req.setAttribute("month", month);
            req.setAttribute("nbDays", ym.lengthOfMonth());
            req.setAttribute("firstDay", firstDayMonth);

            req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
            req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

            req.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(req, res);

        } finally {
            em.close();
        }
    }

    private static int rotateDay(int dow, int startIndex) {
        int rotated = dow - startIndex + 1;
        if (rotated <= 0) rotated += 7;
        return rotated;
    }

    private static int rotationIndex(String raw) {
        if (raw == null) return 1;

        raw = raw.trim().toLowerCase();
        switch (raw) {
            case "lundi": return 1;
            case "mardi": return 2;
            case "mercredi": return 3;
            case "jeudi": return 4;
            case "vendredi": return 5;
            case "samedi": return 6;
            case "dimanche": return 7;
        }
        return 1;
    }
}