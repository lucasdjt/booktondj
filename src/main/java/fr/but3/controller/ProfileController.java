package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.model.User;
import fr.but3.utils.Config;
import fr.but3.repository.UserRepository;
import fr.but3.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ProfileController(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) return "redirect:/login?error=auth";

        User user = userRepository.findById(principal.getUserId()).orElse(null);
        if (user == null) return "redirect:/login?error=auth";

        model.addAttribute("user", user);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "profile";
    }

    @PostMapping("/profile/photo")
    public String uploadPhoto(@RequestParam("file") MultipartFile file,
                              HttpSession session) {

        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) return "redirect:/login?error=auth";

        User user = userRepository.findById(principal.getUserId()).orElse(null);
        if (user == null) return "redirect:/login?error=auth";

        try {
            String filename = fileStorageService.storeProfileImage(user.getId(), file);

            user.setProfileImage(filename);
            userRepository.save(user);

            principal.setProfileImage(filename);
            session.setAttribute("principal", principal);

            return "redirect:/profile?ok=1";
        } catch (Exception e) {
            return "redirect:/profile?error=1";
        }
    }
}