package fr.but3.controller.admin;

import fr.but3.model.User;
import fr.but3.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AdminUsersController {

    private final UserRepository userRepository;

    public AdminUsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll(Sort.by("name").ascending()));
        return "admin/users";
    }

    @PostMapping("/admin/users")
    public String usersAction(@RequestParam String action,
                              @RequestParam Integer id) {

        User u = userRepository.findById(id).orElse(null);
        if (u != null) {
            switch (action) {
                case "delete":
                    userRepository.delete(u);
                    break;
                case "role-admin":
                    u.setRole("ADMIN");
                    userRepository.save(u);
                    break;
                case "role-user":
                    u.setRole("USER");
                    userRepository.save(u);
                    break;
            }
        }
        return "redirect:/admin/users";
    }
}