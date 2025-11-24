package fr.but3.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import fr.but3.dao.BookingDAO;
import fr.but3.utils.Config;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/reserve")
public class ReserveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String dateParam = req.getParameter("date");
        String startParam = req.getParameter("start");

        req.setAttribute("date", dateParam);
        req.setAttribute("start", startParam);

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));
        req.getRequestDispatcher("/WEB-INF/views/reserve.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
            String dateStr = req.getParameter("date");
            String startStr = req.getParameter("start");
            String name     = req.getParameter("name");

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime start = LocalTime.parse(startStr);

            boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));
            int duree = fixedSlot
                    ? Config.getMinutes("planning.creneau")
                    : Config.getMinutes("planning.creneau_min");

            LocalTime end = start.plusMinutes(duree);

            BookingDAO dao = new BookingDAO();
            dao.createBooking(date, start, end, name, 1);

            res.sendRedirect("day?date=" + date);

    }
}
