package fr.but3.service.admin;

import fr.but3.model.User;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<User> users = em.createQuery(
                    "SELECT u FROM User u ORDER BY u.name",
                    User.class
            ).getResultList();

            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp")
                .forward(req, res);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if (idStr == null) {
            res.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        int id = Integer.parseInt(idStr);

        EntityManager em = JPAUtil.getEntityManager();
        try {

            em.getTransaction().begin();
            User u = em.find(User.class, id);
            if (u != null) {
                switch (action) {
                    case "delete":
                        em.remove(u);
                        break;
                    case "role-admin":
                        u.setRole("ADMIN");
                        break;
                    case "role-user":
                        u.setRole("USER");
                        break;
                }
            }
            em.getTransaction().commit();

        } finally {
            em.close();
        }

        res.sendRedirect(req.getContextPath() + "/admin/users");
    }
}