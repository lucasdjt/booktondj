package fr.but3.service;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.utils.Config;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/my-reservations")
public class MyReservationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Principal principal = (session != null)
                ? (Principal) session.getAttribute("principal")
                : null;

        if (principal == null) {
            res.sendRedirect("login?error=auth");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Reservation> reservations = em.createQuery(
                    "SELECT r FROM Reservation r " +
                    "JOIN FETCH r.slot s " +
                    "WHERE r.user.id = :uid " +
                    "ORDER BY s.date, s.startTime",
                    Reservation.class
            )
            .setParameter("uid", principal.getUserId())
            .getResultList();

            req.setAttribute("reservations", reservations);

            req.setAttribute("planningColorPrimary",  Config.get("planning.couleur_principal"));
            req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

            req.getRequestDispatcher("/WEB-INF/views/my-reservations.jsp")
               .forward(req, res);

        } finally {
            em.close();
        }
    }
}