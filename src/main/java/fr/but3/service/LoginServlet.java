package fr.but3.service;

import fr.but3.model.Principal;
import fr.but3.model.User;
import fr.but3.utils.JPAUtil;
import fr.but3.utils.MD5Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String error = req.getParameter("error");
        String redirect = req.getParameter("redirect");

        if (redirect != null) {
            req.setAttribute("redirect", redirect);
            req.setAttribute("sid", req.getParameter("sid"));
            req.setAttribute("date", req.getParameter("date"));
        }

        if (error != null) {
            req.setAttribute("error", true);
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");
        String sidParam = req.getParameter("sid");
        String dateParam = req.getParameter("date");

        if (name == null || password == null || name.isBlank() || password.isBlank()) {
            res.sendRedirect("login?error=1");
            return;
        }

        String hash = MD5Util.md5(password);

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.name = :n AND u.pwd = :h",
                    User.class
            );
            q.setParameter("n", name);
            q.setParameter("h", hash);

            User user = q.getResultStream().findFirst().orElse(null);
            if (user == null) {
                res.sendRedirect("login?error=1");
                return;
            }

            Principal principal = new Principal(user.getId(), user.getName(), user.getRole());
            HttpSession session = req.getSession(true);
            session.setAttribute("principal", principal);

            if ("reserve".equals(redirect) && sidParam != null && dateParam != null) {
                res.sendRedirect("reserve?sid=" + sidParam + "&date=" + dateParam);
            } else {
                res.sendRedirect("calendar");
            }

        } finally {
            em.close();
        }
    }
}