package fr.but3.service.admin;

import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            long userCount = em.createQuery(
                    "SELECT COUNT(u) FROM User u",
                    Long.class
            ).getSingleResult();

            long slotCount = em.createQuery(
                    "SELECT COUNT(s) FROM Slot s",
                    Long.class
            ).getSingleResult();

            long reservationCount = em.createQuery(
                    "SELECT COUNT(r) FROM Reservation r",
                    Long.class
            ).getSingleResult();

            req.setAttribute("users", userCount);
            req.setAttribute("slots", slotCount);
            req.setAttribute("reservations", reservationCount);

            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
                    .forward(req, res);

        } finally {
            em.close();
        }
    }
}