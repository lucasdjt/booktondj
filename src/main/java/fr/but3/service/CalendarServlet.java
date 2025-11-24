package fr.but3.service;

import fr.but3.dao.BookingDAO;
import fr.but3.model.Booking;
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

        BookingDAO dao = new BookingDAO();

        String monthParam = req.getParameter("month");
        String yearParam = req.getParameter("year");

        LocalDate today = LocalDate.now();
        int month = (monthParam == null) ? today.getMonthValue() : Integer.parseInt(monthParam);
        int year  = (yearParam  == null) ? today.getYear()        : Integer.parseInt(yearParam);
        YearMonth ym = YearMonth.of(year, month);

        Set<Integer> joursActifs = Config.getEnabledDays();
        Set<LocalDate> joursFeries = Config.getHolidays(year);

        boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));

        int duree = fixedSlot
                ? Config.getMinutes("planning.creneau")
                : Config.getMinutes("planning.creneau_min");

        int pause = fixedSlot ? 0 : Config.getMinutes("planning.pause_delai");

        int maxPers = Integer.parseInt(Config.get("planning.nb_prsn"));

        LocalTime startGlob = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime endGlob   = LocalTime.parse(Config.get("planning.horaire_final"));
        boolean overnight = endGlob.isBefore(startGlob);

        Map<Integer, JourStats> stats = new HashMap<>();

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {

            LocalDate date = LocalDate.of(year, month, day);
            int dow = date.getDayOfWeek().getValue();

            boolean ouvert = joursActifs.contains(dow);
            boolean ferie  = joursFeries.contains(date);

            int maxDelay = Config.getMaxReservationDays();
            boolean limiteDepassee = date.isAfter(today.plusDays(maxDelay));

            List<Booking> bookings = dao.getBookingsForDay(date);

            int totalPersonnes = bookings.stream().mapToInt(Booking::getNbPersonnes).sum();

            int creneauxDispo = 0;
            int totalCreneaux = 0;

            if (ouvert && !ferie) {

                LocalDateTime cursor = LocalDateTime.of(date, startGlob);
                LocalDateTime endLimit = LocalDateTime.of(date, endGlob);

                if (overnight) endLimit = endLimit.plusDays(1);

                while (!cursor.isAfter(endLimit)) {

                    LocalDateTime slotEnd = cursor.plusMinutes(duree);
                    if (slotEnd.isAfter(endLimit.plusSeconds(1))) break;

                    int inscrits = 0;

                    for (Booking b : bookings) {
                        LocalDateTime bStart = LocalDateTime.of(date, b.getTimeStart());
                        LocalDateTime bEnd   = LocalDateTime.of(date, b.getTimeEnd());

                        if (overnight) {
                            if (bStart.toLocalTime().isBefore(startGlob)) bStart = bStart.plusDays(1);
                            if (bEnd.toLocalTime().isBefore(startGlob)) bEnd = bEnd.plusDays(1);
                            if (bEnd.isBefore(bStart)) bEnd = bEnd.plusDays(1);
                        }

                        boolean overlap =
                                cursor.isBefore(bEnd) &&
                                bStart.isBefore(slotEnd);

                        if (overlap) {
                            inscrits += b.getNbPersonnes();
                        }

                    }

                    if (inscrits < maxPers) creneauxDispo++;
                    totalCreneaux++;

                    cursor = slotEnd.plusMinutes(pause);
                }
            }

            double taux = (totalCreneaux > 0)
                    ? (double) totalPersonnes / (maxPers * totalCreneaux)
                    : 0;

            taux = Math.max(0, Math.min(1, taux));

            stats.put(day, new JourStats(
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
