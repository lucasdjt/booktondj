package fr.but3.service;

import fr.but3.dao.BookingDAO;
import fr.but3.model.Booking;
import fr.but3.utils.Creneau;
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

        String dateParam = req.getParameter("date");
        if (dateParam == null) {
            res.sendRedirect("calendar");
            return;
        }

        String mode = req.getParameter("mode");
        if (mode == null) mode = "creneaux";

        LocalDate date = LocalDate.parse(dateParam);

        BookingDAO dao = new BookingDAO();
        List<Booking> bookingsToday = dao.getBookingsForDay(date);
        List<Booking> bookingsTomorrow = dao.getBookingsForDay(date.plusDays(1));

        LocalTime startGlob = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime endGlob   = LocalTime.parse(Config.get("planning.horaire_final"));
        boolean overnight = endGlob.isBefore(startGlob);

        boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));
        int duree = fixedSlot
                ? Config.getMinutes("planning.creneau")
                : Config.getMinutes("planning.creneau_min");

        int pause = fixedSlot ? 0 : Config.getMinutes("planning.pause_delai");
        int maxPers = Integer.parseInt(Config.get("planning.nb_prsn"));

        Set<Integer> joursActifs = Config.getEnabledDays();
        int maxDelay = Config.getMaxReservationDays();
        LocalDate today = LocalDate.now();

        List<Creneau> flatList = new ArrayList<>();

        LocalDateTime cursor = LocalDateTime.of(date, startGlob);
        LocalDateTime endLimit = LocalDateTime.of(date, endGlob);
        if (overnight) endLimit = endLimit.plusDays(1);

        while (!cursor.isAfter(endLimit)) {

            LocalDateTime slotEnd = cursor.plusMinutes(duree);
            if (slotEnd.isAfter(endLimit.plusSeconds(1))) break;

            int inscrits = 0;

            for (Booking b : bookingsToday) {
                LocalDateTime bStart = LocalDateTime.of(date, b.getTimeStart());
                LocalDateTime bEnd   = LocalDateTime.of(date, b.getTimeEnd());
                if (bEnd.isBefore(bStart)) bEnd = bEnd.plusDays(1);

                if (!bStart.isAfter(slotEnd) && !bEnd.isBefore(cursor)) {
                    inscrits += b.getNbPersonnes();
                }
            }

            if (overnight) {
                for (Booking b : bookingsTomorrow) {
                    LocalDateTime bStart = LocalDateTime.of(date.plusDays(1), b.getTimeStart());
                    LocalDateTime bEnd   = LocalDateTime.of(date.plusDays(1), b.getTimeEnd());
                    if (bEnd.isBefore(bStart)) bEnd = bEnd.plusDays(1);

                    if (!bStart.isAfter(slotEnd) && !bEnd.isBefore(cursor)) {
                        inscrits += b.getNbPersonnes();
                    }
                }
            }

            LocalDate dateReelle;
            if (!overnight) {
                dateReelle = date;
            } else {
                if (cursor.toLocalTime().isBefore(startGlob)) {
                    dateReelle = date.plusDays(1);
                } else {
                    dateReelle = date;
                }
            }

            Creneau c = new Creneau(cursor.toLocalTime(), slotEnd.toLocalTime(), dateReelle);
            c.nbInscrits = inscrits;
            c.maxPers = maxPers;
            c.complet = (inscrits >= maxPers);

            int dow = dateReelle.getDayOfWeek().getValue();
            boolean ouvert = joursActifs.contains(dow);
            Set<LocalDate> holidaysRealYear = Config.getHolidays(dateReelle.getYear());
            boolean ferie = holidaysRealYear.contains(dateReelle);
            boolean limiteDepassee = dateReelle.isAfter(today.plusDays(maxDelay));

            c.reservable = ouvert && !ferie && !limiteDepassee;

            flatList.add(c);

            cursor = slotEnd.plusMinutes(pause);
        }

        Map<Integer, List<Creneau>> timeline = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) timeline.put(h, new ArrayList<>());

        cursor = LocalDateTime.of(date, startGlob);
        LocalDateTime endLimitTimeline = LocalDateTime.of(date, endGlob);
        if (overnight) {
            endLimitTimeline = LocalDateTime.of(date, LocalTime.MAX);
        }

        int dowDate = date.getDayOfWeek().getValue();
        boolean ouvertDate = joursActifs.contains(dowDate);
        Set<LocalDate> holidaysDateYear = Config.getHolidays(date.getYear());
        boolean ferieDate = holidaysDateYear.contains(date);
        boolean limiteDate = date.isAfter(today.plusDays(maxDelay));
        boolean reservableJour = ouvertDate && !ferieDate && !limiteDate;

        while (!cursor.isAfter(endLimitTimeline)) {

            LocalDateTime slotEnd = cursor.plusMinutes(duree);
            if (slotEnd.isAfter(endLimitTimeline.plusSeconds(1))) break;

            int inscrits = 0;

            for (Booking b : bookingsToday) {
                LocalDateTime bStart = LocalDateTime.of(date, b.getTimeStart());
                LocalDateTime bEnd   = LocalDateTime.of(date, b.getTimeEnd());
                if (bEnd.isBefore(bStart)) bEnd = bEnd.plusDays(1);

                if (!bStart.isAfter(slotEnd) && !bEnd.isBefore(cursor)) {
                    inscrits += b.getNbPersonnes();
                }
            }

            Creneau c = new Creneau(cursor.toLocalTime(), slotEnd.toLocalTime(), date);
            c.nbInscrits = inscrits;
            c.maxPers = maxPers;
            c.complet = (inscrits >= maxPers);
            c.reservable = reservableJour;

            timeline.get(cursor.getHour()).add(c);

            cursor = slotEnd.plusMinutes(pause);
        }

        req.setAttribute("mode", mode);
        req.setAttribute("date", date);
        req.setAttribute("creneaux", flatList);
        req.setAttribute("timeline", timeline);
        req.setAttribute("start", startGlob);
        req.setAttribute("end", endGlob);
        req.setAttribute("overnight", overnight);

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/day.jsp").forward(req, res);
    }
}