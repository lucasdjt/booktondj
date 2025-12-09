package fr.but3.service;

import fr.but3.model.Reservation;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/delete-reservation")
public class AdminDeleteReservationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null) {
            res.sendRedirect("dashboard");
            return;
        }

        int rid;
        try {
            rid = Integer.parseInt(idStr);
        } catch (Exception e) {
            res.sendRedirect("dashboard");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Reservation r = em.find(Reservation.class, rid);
            if (r != null) {
                em.remove(r);
            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }

        res.sendRedirect("dashboard");
    }
}