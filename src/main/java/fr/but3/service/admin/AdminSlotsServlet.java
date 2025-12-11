package fr.but3.service.admin;

import fr.but3.model.Slot;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/slots")
public class AdminSlotsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Slot> slots = em.createQuery(
                    "SELECT s FROM Slot s ORDER BY s.date, s.startTime",
                    Slot.class
            ).getResultList();

            req.setAttribute("slots", slots);
            req.getRequestDispatcher("/WEB-INF/views/admin/slots.jsp")
                .forward(req, res);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        EntityManager em = JPAUtil.getEntityManager();
        try {

            em.getTransaction().begin();

            switch (action) {

                case "delete-slot":
                    int sid = Integer.parseInt(req.getParameter("sid"));
                    Slot s = em.find(Slot.class, sid);
                    if (s != null) em.remove(s);
                    break;

                case "delete-day":
                    LocalDate date = LocalDate.parse(req.getParameter("date"));
                    em.createQuery("DELETE FROM Slot s WHERE s.date = :d")
                            .setParameter("d", date)
                            .executeUpdate();
                    break;

            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }

        res.sendRedirect(req.getContextPath() + "/admin/slots");
    }
}