package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.repository.ReservationRepository;
import fr.but3.service.ReservationService;
import fr.but3.utils.Config;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MyReservationsController {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    public MyReservationsController(ReservationRepository reservationRepository,
                                    ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    @GetMapping("/my-reservations")
    public String myReservations(
            @RequestParam(name = "filter", required = false, defaultValue = "all") String filter,
            HttpSession session,
            Model model
    ) {
        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login?error=auth";
        }

        Integer uid = principal.getUserId();
        LocalDate today = LocalDate.now();

        List<Reservation> reservations;
        switch (filter.toLowerCase()) {
            case "past":
                reservations = reservationRepository
                        .findPastByUserIdOrderBySlotDateAscSlotStartTimeAsc(uid, today);
                break;
            case "future":
                reservations = reservationRepository
                        .findFutureByUserIdOrderBySlotDateAscSlotStartTimeAsc(uid, today);
                break;
            case "all":
            default:
                filter = "all";
                reservations = reservationRepository
                        .findByUserIdOrderBySlotDateAscSlotStartTimeAsc(uid);
                break;
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("filter", filter);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "my-reservations";
    }

    @PostMapping("/my-reservations/delete")
    public String deleteReservation(@RequestParam(required = false) Integer id,
                                    @RequestParam(name = "filter", required = false, defaultValue = "all") String filter,
                                    HttpSession session) {

        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login?error=auth";
        }
        if (id == null) {
            return "redirect:/my-reservations?filter=" + filter;
        }

        try {
            reservationService.cancelReservation(id, principal.getUserId(), principal.isAdmin());
        } catch (ReservationService.ReservationException ex) {
            return "redirect:/my-reservations?filter=" + filter + "&error=" + ex.getCode();
        }

        return "redirect:/my-reservations?filter=" + filter;
    }
}