package fr.but3.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import fr.but3.utils.Config;

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

        LocalDate date = LocalDate.parse(dateParam);

        String hDebut = Config.get("planning.horaire_debut");
        String hFin = Config.get("planning.horaire_final");

        LocalTime tStart = LocalTime.parse(hDebut);
        LocalTime tEnd = LocalTime.parse(hFin);

        List<LocalTime> hours = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            hours.add(LocalTime.of(h, 0));
        }

        req.setAttribute("date", date);
        req.setAttribute("hours", hours);
        req.setAttribute("tStart", tStart);
        req.setAttribute("tEnd", tEnd);

        req.getRequestDispatcher("/WEB-INF/views/day.jsp").forward(req, res);
    }
}
