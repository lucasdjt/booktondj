package fr.but3.service;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/my-reservations/delete")
public class CancelReservationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Principal principal = (session != null)
                ? (Principal) session.getAttribute("principal")
                : null;

        if (principal == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=auth");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null) {
            res.sendRedirect("my-reservations");
            return;
        }

        int rid;
        try {
            rid = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            res.sendRedirect("my-reservations");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Reservation r = em.find(Reservation.class, rid);
            if (r != null) {
                boolean canDelete =
                        principal.isAdmin() ||
                        r.getUser().getId() == principal.getUserId();

                if (canDelete) {
                    em.remove(r);
                }
            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }
        res.sendRedirect(req.getContextPath() + "/my-reservations");
    }
}