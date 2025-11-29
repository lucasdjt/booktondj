package fr.but3.service;

import fr.but3.dao.ReservationDAO;
import fr.but3.dao.SlotDAO;
import fr.but3.dao.UserDAO;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.utils.Config;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

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
        String name    = req.getParameter("name");

        int sid = Integer.parseInt(sidStr);

        UserDAO userDAO = new UserDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        SlotDAO slotDAO = new SlotDAO();

        User u = userDAO.findOrCreateByName(name);
        Slot s = slotDAO.getById(sid);

        int used = reservationDAO.getUsedCapacityForSlot(sid);
        int remaining = s.getCapacity() - used;

        if (remaining <= 0) {
            res.sendRedirect("day?date=" + dateStr + "&error=full");
            return;
        }

        reservationDAO.createReservation(sid, u.getId(), 1);

        res.sendRedirect("day?date=" + dateStr);
    }
}
