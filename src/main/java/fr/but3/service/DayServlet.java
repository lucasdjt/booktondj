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
        List<Booking> bookingsToday     = dao.getBookingsForDay(date);
        List<Booking> bookingsTomorrow  = dao.getBookingsForDay(date.plusDays(1));

        LocalTime startGlob = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime endGlob   = LocalTime.parse(Config.get("planning.horaire_final"));
        boolean overnight   = endGlob.isBefore(startGlob);

        boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));

        int duree = fixedSlot
                ? Config.getMinutes("planning.creneau")
                : Config.getMinutes("planning.creneau_min");

        int pause = fixedSlot ? 0 : Config.getMinutes("planning.pause_delai");
        int maxPers = Integer.parseInt(Config.get("planning.nb_prsn"));

        List<Creneau> flatList = new ArrayList<>();
        Map<Integer, List<Creneau>> timeline = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) {
            timeline.put(h, new ArrayList<>());
        }

        LocalDateTime cursor   = LocalDateTime.of(date, startGlob);
        LocalDateTime endLimit = LocalDateTime.of(date, endGlob);
        if (overnight) {
            endLimit = endLimit.plusDays(1);
        }

        while (!cursor.isAfter(endLimit)) {

            LocalDateTime slotEnd = cursor.plusMinutes(duree);
            if (slotEnd.isAfter(endLimit.plusSeconds(1))) {
                break;
            }

            boolean isTomorrowSlot = overnight && cursor.toLocalTime().isBefore(startGlob);

            List<Booking> bookedList = isTomorrowSlot ? bookingsTomorrow : bookingsToday;

            int inscrits = 0;

            for (Booking b : bookedList) {
                LocalDate bDate = b.getDate();
                LocalDateTime bStart = LocalDateTime.of(bDate, b.getTimeStart());
                LocalDateTime bEnd   = LocalDateTime.of(bDate, b.getTimeEnd());

                if (bEnd.isBefore(bStart)) {
                    bEnd = bEnd.plusDays(1);
                }

                if (bStart.isBefore(slotEnd) && bEnd.isAfter(cursor)) {
                    inscrits += b.getNbPersonnes();
                }
            }

            Creneau c = new Creneau(cursor.toLocalTime(), slotEnd.toLocalTime());
            c.nbInscrits = inscrits;
            c.maxPers    = maxPers;
            c.complet    = (inscrits >= maxPers);

            c.dateReelle = isTomorrowSlot ? date.plusDays(1) : date;

            flatList.add(c);
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

        req.setAttribute("planningColorPrimary",   Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/day.jsp").forward(req, res);
    }
}
