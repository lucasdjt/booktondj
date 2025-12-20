package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.model.User;
import fr.but3.repository.UserRepository;
import fr.but3.utils.MD5Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        if (name == null || password == null || name.isBlank() || password.isBlank()) {
            return "redirect:/login?error=1";
        }

        String hash = MD5Util.md5(password);

        User user = userRepository.findByNameAndPwd(name, hash).orElse(null);
        if (user == null) {
            return "redirect:/login?error=1";
        }

        Principal principal = new Principal(user.getId(), user.getName(), user.getRole());
        session.setAttribute("principal", principal);

        if ("reserve".equals(redirect) && sid != null && date != null) {
            return "redirect:/reserve?sid=" + sid + "&date=" + date;
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

        if (name == null || password == null || confirm == null ||
                name.isBlank() || password.isBlank() || confirm.isBlank()) {
            return "redirect:/register?error=missing";
        }

        if (!password.equals(confirm)) {
            return "redirect:/register?error=nomatch";
        }

        if (userRepository.existsByName(name)) {
            return "redirect:/register?error=exists";
        }

        String hash = MD5Util.md5(password);
        User user = new User(name, hash, "USER");
        user = userRepository.save(user);

        Principal p = new Principal(user.getId(), user.getName(), user.getRole());
        session.setAttribute("principal", p);

        return "redirect:/calendar";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/calendar";
    }
}