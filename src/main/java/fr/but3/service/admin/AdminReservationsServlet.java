package fr.but3.service.admin;

import fr.but3.model.Reservation;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reservations")
public class AdminReservationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Reservation> reservations = em.createQuery(
                    "SELECT r FROM Reservation r " +
                    "JOIN FETCH r.slot s " +
                    "JOIN FETCH r.user u " +
                    "ORDER BY s.date, s.startTime",
                    Reservation.class
            ).getResultList();

            req.setAttribute("reservations", reservations);
            req.getRequestDispatcher("/WEB-INF/views/admin/reservations.jsp")
                .forward(req, res);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int rid = Integer.parseInt(req.getParameter("rid"));

        EntityManager em = JPAUtil.getEntityManager();
        try {

            em.getTransaction().begin();
            Reservation r = em.find(Reservation.class, rid);
            if (r != null) em.remove(r);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

        res.sendRedirect(req.getContextPath() + "/admin/reservations");
    }
}