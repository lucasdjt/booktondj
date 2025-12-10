package fr.but3.service;

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
            List<Object[]> users = em.createQuery(
                    "SELECT u.id, u.name, u.role FROM User u", Object[].class
            ).getResultList();

            List<Object[]> slots = em.createQuery(
                    "SELECT s.id, s.date, s.startTime, s.endTime, s.capacity FROM Slot s",
                    Object[].class
            ).getResultList();

            List<Object[]> reservations = em.createQuery(
                    "SELECT r.id, r.user.name, r.slot.date, r.slot.startTime, r.nbPersonnes FROM Reservation r",
                    Object[].class
            ).getResultList();

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