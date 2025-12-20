package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.repository.ReservationRepository;
import fr.but3.utils.Config;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MyReservationsController {

    private final ReservationRepository reservationRepository;

    public MyReservationsController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/my-reservations")
    public String myReservations(HttpSession session, Model model) {
        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login?error=auth";
        }

        List<Reservation> reservations =
                reservationRepository.findByUserIdOrderBySlotDateAscSlotStartTimeAsc(principal.getUserId());

        model.addAttribute("reservations", reservations);
        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "my-reservations";
    }

    @PostMapping("/my-reservations/delete")
    public String deleteReservation(@RequestParam(required = false) Integer id,
                                    HttpSession session) {

        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login?error=auth";
        }
        if (id == null) {
            return "redirect:/my-reservations";
        }

        reservationRepository.findById(id).ifPresent(r -> {
            boolean canDelete = principal.isAdmin()
                    || (r.getUser() != null && r.getUser().getId() != null
                        && r.getUser().getId().intValue() == principal.getUserId());
            if (canDelete) {
                reservationRepository.delete(r);
            }
        });

        return "redirect:/my-reservations";
    }
}