package fr.but3;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String monthParameters = request.getParameter("month");
        String yearParameters = request.getParameter("year");
        LocalDate now = LocalDate.now();
        int month = (monthParameters == null) ? now.getMonthValue() : Integer.parseInt(monthParameters);
        int year = (yearParameters == null) ? now.getYear() : Integer.parseInt(yearParameters);
        YearMonth yearMonth = YearMonth.of(year, month);

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("nbDays", yearMonth.lengthOfMonth());
        request.setAttribute("firstDay", yearMonth.atDay(1).getDayOfWeek().getValue());

        request.getRequestDispatcher("/calendar.jsp").forward(request, response);
    }
}
