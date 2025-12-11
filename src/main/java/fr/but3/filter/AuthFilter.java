package fr.but3.filter;

import fr.but3.model.Principal;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/reserve",
        "/my-reservations",
        "/my-reservations/delete",
        "/profile"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse s = (HttpServletResponse) res;

        Principal p = (Principal) r.getSession().getAttribute("principal");

        if (p == null) {
            s.sendRedirect(r.getContextPath() + "/login?error=auth");
            return;
        }

        chain.doFilter(req, res);
    }
}