package fr.but3.service;

import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;

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

        SlotRepository slotRepo = new SlotRepository();
        ReservationRepository resRepo = new ReservationRepository();

        LocalDate today = LocalDate.now();
        String mode = Optional.ofNullable(req.getParameter("mode")).orElse("creneaux");

        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            res.sendRedirect("calendar");
            return;
        }

        LocalDate date = LocalDate.parse(dateParam);

        List<Slot> slots = slotRepo.getSlotsForDay(date);

        int maxDelay = Config.getMaxReservationDays();
        boolean limiteDepassee = date.isAfter(today.plusDays(maxDelay));

        Set<Integer> joursActifs = Config.getEnabledDays();
        boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());

        Set<LocalDate> holidays = Config.getHolidays(date.getYear());
        boolean ferie = holidays.contains(date);

        List<Map<String,Object>> viewCreneaux = new ArrayList<>();

        for (Slot s : slots) {

            int used = resRepo.getUsedCapacityForSlot(s.getId());
            boolean complet = used >= s.getCapacity();

            boolean reservable =
                    !complet &&
                    !limiteDepassee &&
                    ouvert &&
                    !ferie;

            Map<String,Object> map = new HashMap<>();
            map.put("slot", s);
            map.put("used", used);
            map.put("complet", complet);
            map.put("reservable", reservable);

            viewCreneaux.add(map);
        }

        req.setAttribute("creneaux", viewCreneaux);
        req.setAttribute("mode", mode);
        req.setAttribute("date", date);

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/day.jsp").forward(req, res);
    }
}