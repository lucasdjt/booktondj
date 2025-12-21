package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String redirect,
                            @RequestParam(required = false) String sid,
                            @RequestParam(required = false) String date,
                            Model model) {

        if (redirect != null) {
            model.addAttribute("redirect", redirect);
            model.addAttribute("sid", sid);
            model.addAttribute("date", date);
        }
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name,
                        @RequestParam String password,
                        @RequestParam(required = false) String redirect,
                        @RequestParam(required = false) String sid,
                        @RequestParam(required = false) String date,
                        HttpSession session) {

        final Principal principal;
        try {
            principal = authService.authenticate(name, password);
        } catch (AuthService.AuthException ex) {
            return "redirect:/login?error=1";
        }

        session.setAttribute("principal", principal);

        if ("reserve".equals(redirect) && sid != null && date != null) {
            return UriComponentsBuilder
                    .fromPath("/reserve")
                    .queryParam("sid", sid)
                    .queryParam("date", date)
                    .build()
                    .toUriString();
        }

        return "redirect:/calendar";
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", error);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String password,
                           @RequestParam String confirm,
                           HttpSession session) {

        final Principal p;
        try {
            p = authService.register(name, password, confirm);
        } catch (AuthService.AuthException ex) {
            return "redirect:/register?error=" + ex.getCode();
        }

        session.setAttribute("principal", p);
        return "redirect:/calendar";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/calendar";
    }
}