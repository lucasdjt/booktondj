package fr.but3.service;

import fr.but3.model.Reservation;
import fr.but3.utils.Config;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Reservation> all = em.createQuery(
                    "SELECT r FROM Reservation r " +
                    "JOIN FETCH r.slot s " +
                    "JOIN FETCH r.user u " +
                    "ORDER BY s.date, s.startTime",
                    Reservation.class
            ).getResultList();

            req.setAttribute("reservations", all);
            req.setAttribute("planningColorPrimary",  Config.get("planning.couleur_principal"));
            req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

            req.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp")
               .forward(req, res);

        } finally {
            em.close();
        }
    }
}