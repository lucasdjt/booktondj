package fr.but3.service;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.utils.Config;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/reserve")
public class ReserveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String sidParam = req.getParameter("sid");
        String dateParam = req.getParameter("date");

        req.setAttribute("sid", sidParam);
        req.setAttribute("date", dateParam);

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/reserve.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String sidStr  = req.getParameter("sid");
        String dateStr = req.getParameter("date");

        if (sidStr == null || dateStr == null) {
            res.sendRedirect("calendar");
            return;
        }

        int sid = Integer.parseInt(sidStr);

        HttpSession session = req.getSession(false);
        Principal principal = (session != null)
                ? (Principal) session.getAttribute("principal")
                : null;

        if (principal == null) {
            res.sendRedirect("login?redirect=reserve&sid=" + sidStr + "&date=" + dateStr);
            return;
        }
        LocalDate date = LocalDate.parse(dateStr);
        LocalDate today = LocalDate.now();
        int maxDelay = Config.getMaxReservationDays();

        boolean limiteDepassee =
                date.isAfter(today.plusDays(maxDelay)) ||
                date.isBefore(today);

        if (limiteDepassee) {
            res.sendRedirect("day?date=" + dateStr + "&error=expired");
            return;
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Slot slot = em.find(Slot.class, sid);
            if (slot == null) {
                res.sendRedirect("day?date=" + dateStr + "&error=notslot");
                return;
            }

            User user = em.find(User.class, principal.getUserId());
            if (user == null) {
                res.sendRedirect("login");
                return;
            }

            Long usedLong = em.createQuery(
                    "SELECT COALESCE(SUM(r.nbPersonnes),0) " +
                    "FROM Reservation r WHERE r.slot.id = :sid",
                    Long.class
            )
            .setParameter("sid", sid)
            .getSingleResult();

            int used = usedLong.intValue();
            int remaining = slot.getCapacity() - used;

            if (remaining <= 0) {
                res.sendRedirect("day?date=" + dateStr + "&error=full");
                return;
            }

            Reservation r = new Reservation(slot, user, 1);

            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();

            res.sendRedirect("day?date=" + dateStr);

        } finally {
            em.close();
        }
    }
}