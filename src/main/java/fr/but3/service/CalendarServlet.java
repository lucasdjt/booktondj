package fr.but3.service;

import fr.but3.model.JourStats;
import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.*;
import java.util.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        SlotRepository slotRepo = new SlotRepository();
        ReservationRepository resRepo = new ReservationRepository();

        LocalDate today = LocalDate.now();

        int month = Optional.ofNullable(req.getParameter("month"))
                .map(Integer::parseInt)
                .orElse(today.getMonthValue());

        int year = Optional.ofNullable(req.getParameter("year"))
                .map(Integer::parseInt)
                .orElse(today.getYear());

        YearMonth ym = YearMonth.of(year, month);

        Map<Integer, JourStats> stats = new HashMap<>();

        Set<Integer> joursActifs = Config.getEnabledDays();
        Set<LocalDate> joursFeries = Config.getHolidays(year);

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {

            LocalDate date = LocalDate.of(year, month, d);

            boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());
            boolean ferie = joursFeries.contains(date);
            boolean limite = date.isAfter(today.plusDays(Config.getMaxReservationDays()));

            List<Slot> slots = slotRepo.getSlotsForDay(date);

            int dispo = 0;
            int totalUsed = 0;

            for (Slot s : slots) {
                int used = resRepo.getUsedCapacityForSlot(s.getId());
                totalUsed += used;

                if (used < s.getCapacity()) dispo++;
            }

            double taux =
                    (slots.isEmpty())
                            ? 0
                            : (double) totalUsed / (slots.size() * slots.get(0).getCapacity());

            taux = Math.max(0, Math.min(1, taux));

            stats.put(d, new JourStats(dispo, totalUsed, ouvert, ferie, taux, limite));
        }

        req.setAttribute("stats", stats);
        req.setAttribute("year", year);
        req.setAttribute("month", month);
        req.setAttribute("nbDays", ym.lengthOfMonth());

        req.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(req, res);
    }
}