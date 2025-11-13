package fr.but3.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import fr.but3.dao.DayClickDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        DayClickDAO dao = new DayClickDAO();

        String monthParameters = req.getParameter("month");
        String yearParameters = req.getParameter("year");

        LocalDate now = LocalDate.now();
        int month = (monthParameters == null) ? now.getMonthValue() : Integer.parseInt(monthParameters);
        int year = (yearParameters == null) ? now.getYear() : Integer.parseInt(yearParameters);
        YearMonth yearMonth = YearMonth.of(year, month);

        String clicked = req.getParameter("clickedDate");
        if (clicked != null) {
            LocalDate clickedDate = LocalDate.parse(clicked);
            dao.increment(clickedDate);
        }
        Map<Integer, Integer> counters = new HashMap<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            counters.put(day, dao.getCounter(date));
        }    
        req.setAttribute("year", year);
        req.setAttribute("month", month);
        req.setAttribute("nbDays", yearMonth.lengthOfMonth());
        req.setAttribute("firstDay", yearMonth.atDay(1).getDayOfWeek().getValue());
        req.setAttribute("counters", counters);

        req.getRequestDispatcher("/WEB-INF/views/calendar.jsp").forward(req, res);
    }
}
