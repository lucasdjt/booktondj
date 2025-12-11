package fr.but3.service.admin;

import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/db")
public class AdminDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Object[]> users = em.createNativeQuery("SELECT * FROM users").getResultList();
            List<Object[]> slots = em.createNativeQuery("SELECT * FROM slots").getResultList();
            List<Object[]> reservations = em.createNativeQuery("SELECT * FROM reservations").getResultList();

            req.setAttribute("users", users);
            req.setAttribute("slots", slots);
            req.setAttribute("reservations", reservations);

            req.getRequestDispatcher("/WEB-INF/views/admin/db.jsp")
                .forward(req, res);

        } finally {
            em.close();
        }
    }
}