package fr.but3.controller.admin;

import fr.but3.repository.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminReservationsController {

    private final ReservationRepository reservationRepository;

    public AdminReservationsController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/admin/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAllWithSlotAndUser());
        return "admin/reservations";
    }

    @PostMapping("/admin/reservations")
    public String deleteReservation(@RequestParam Integer rid) {
        reservationRepository.deleteById(rid);
        return "redirect:/admin/reservations";
    }
}