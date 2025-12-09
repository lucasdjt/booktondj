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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String error = req.getParameter("error");
        if (error != null) {
            req.setAttribute("error", error);
        }

        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirm");

        if (name == null || password == null || confirm == null ||
            name.isBlank() || password.isBlank() || confirm.isBlank()) {
            res.sendRedirect("register?error=missing");
            return;
        }

        if (!password.equals(confirm)) {
            res.sendRedirect("register?error=nomatch");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.name = :n", User.class);
            q.setParameter("n", name);

            if (!q.getResultList().isEmpty()) {
                res.sendRedirect("register?error=exists");
                return;
            }

            String hash = MD5Util.md5(password);
            User user = new User(name, hash, "USER");

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            Principal p = new Principal(user.getId(), user.getName(), user.getRole());
            HttpSession session = req.getSession(true);
            session.setAttribute("principal", p);

            res.sendRedirect("calendar");

        } finally {
            em.close();
        }
    }
}