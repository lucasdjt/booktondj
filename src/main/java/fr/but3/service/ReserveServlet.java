package fr.but3.service;

import fr.but3.model.Reservation;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.repository.UserRepository;
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

        req.setAttribute("sid", req.getParameter("sid"));
        req.setAttribute("date", req.getParameter("date"));

        req.setAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        req.setAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        req.getRequestDispatcher("/WEB-INF/views/reserve.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int sid = Integer.parseInt(req.getParameter("sid"));
        String dateStr = req.getParameter("date");
        String name = req.getParameter("name");

        UserRepository userRepo = new UserRepository();
        SlotRepository slotRepo = new SlotRepository();
        ReservationRepository resRepo = new ReservationRepository();

        User u = userRepo.findByName(name);
        if (u == null) {
            u = new User(name);
            userRepo.save(u);
        }

        Slot s = slotRepo.find(sid);

        int used = resRepo.getUsedCapacityForSlot(sid);
        if (used >= s.getCapacity()) {
            res.sendRedirect("day?date=" + dateStr + "&error=full");
            return;
        }

        Reservation r = new Reservation(s, u, 1);
        resRepo.save(r);

        res.sendRedirect("day?date=" + dateStr);
    }
}