package fr.but3.controller.admin;

import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public AdminDashboardController(UserRepository userRepository,
                                    SlotRepository slotRepository,
                                    ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("users", userRepository.count());
        model.addAttribute("slots", slotRepository.count());
        model.addAttribute("reservations", reservationRepository.count());

        return "admin/dashboard";
    }
}