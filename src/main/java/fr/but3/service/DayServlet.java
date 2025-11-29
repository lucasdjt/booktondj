package fr.but3.service;

import fr.but3.dao.SlotDAO;
import fr.but3.dao.ReservationDAO;
import fr.but3.model.Slot;
import fr.but3.utils.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.*;
import java.util.*;

@WebServlet("/day")
public class DayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        SlotDAO slotDAO = new SlotDAO();
        ReservationDAO resDAO = new ReservationDAO();

        LocalDate today = LocalDate.now();
        String mode = Optional.ofNullable(req.getParameter("mode")).orElse("creneaux");

        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            res.sendRedirect("calendar");
            return;
        }

        LocalDate date = LocalDate.parse(dateParam);

        if (date.isBefore(today)) {
            res.sendRedirect("calendar");
            return;
        }

        List<Slot> slots = slotDAO.getSlotsForDay(date);

        int maxDelay = Config.getMaxReservationDays();
        LocalDate limitDate = today.plusDays(maxDelay);
        boolean limiteDepassee = date.isBefore(today) || date.isAfter(limitDate);

        Set<Integer> joursActifs = Config.getEnabledDays();
        boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());

        Set<LocalDate> holidays = Config.getHolidays(date.getYear());
        boolean ferie = holidays.contains(date);

        List<Map<String,Object>> viewCreneaux = new ArrayList<>();

        for (Slot s : slots) {

            int used = resDAO.countPersonsForSlot(s.getId());
            boolean complet = used >= s.getCapacity();

            boolean reservable =
                    !complet &&
                    !limiteDepassee &&
                    ouvert &&
                    !ferie;

            Map<String,Object> m = new HashMap<>();
            m.put("slot", s);
            m.put("used", used);
            m.put("complet", complet);
            m.put("reservable", reservable);

            viewCreneaux.add(m);
        }

        Map<Integer, List<Map<String,Object>>> hours = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) {
            hours.put(h, new ArrayList<>());
        }

        for (Map<String,Object> m : viewCreneaux) {
            Slot s = (Slot) m.get("slot");
            int hour = s.getStartTime().getHour();
            hours.get(hour).add(m);
        }

        req.setAttribute("creneaux", viewCreneaux);
        req.setAttribute("hours", hours);

        req.setAttribute("mode", mode);
        req.setAttribute("date", date);

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/day.jsp").forward(req, res);
    }
}