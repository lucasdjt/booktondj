package fr.but3.service;

import fr.but3.dao.SlotDAO;
import fr.but3.dao.ReservationDAO;
import fr.but3.model.Slot;
import fr.but3.model.JourStats;
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

        SlotDAO slotDAO = new SlotDAO();
        ReservationDAO resDAO = new ReservationDAO();

        LocalDate today = LocalDate.now();

        String m = req.getParameter("month");
        String y = req.getParameter("year");

        int month = (m == null) ? today.getMonthValue() : Integer.parseInt(m);
        int year  = (y == null) ? today.getYear()        : Integer.parseInt(y);

        YearMonth ym = YearMonth.of(year, month);

        int maxDelay = Config.getMaxReservationDays();
        LocalDate limitDate = today.plusDays(maxDelay);

        Set<Integer> joursActifs = Config.getEnabledDays();
        Set<LocalDate> joursFeries = Config.getHolidays(year);

        Map<Integer, Integer> usedMonth = resDAO.getUsedCapacityForMonth(year, month);

        Map<Integer, JourStats> stats = new HashMap<>();

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {

            LocalDate date = LocalDate.of(year, month, d);

            boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());
            boolean ferie  = joursFeries.contains(date);

            boolean past = date.isBefore(today);
            boolean limiteDepassee = past || date.isAfter(limitDate);

            List<Slot> slots = slotDAO.getSlotsForDay(date);

            int creneauxDispo = 0;
            int totalPersonnes = 0;

            if (!limiteDepassee && ouvert && !ferie) {
                for (Slot s : slots) {
                    int used = usedMonth.getOrDefault(s.getId(), 0);
                    if (used < s.getCapacity()) creneauxDispo++;
                    totalPersonnes += used;
                }
            }

            double taux;
            if (slots.isEmpty() || limiteDepassee || !ouvert || ferie) {
                taux = 0;
            } else {
                int capacityDay = slots.get(0).getCapacity() * slots.size();
                taux = capacityDay == 0 ? 0 : (double) totalPersonnes / capacityDay;
            }

            taux = Math.max(0, Math.min(1, taux));

            stats.put(d, new JourStats(
                    creneauxDispo,
                    totalPersonnes,
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
    }

    private static int rotateDay(int dow, int startIndex) {
        int r = dow - startIndex + 1;
        if (r <= 0) r += 7;
        return r;
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