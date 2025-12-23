package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.service.ReservationService;
import fr.but3.utils.Config;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Controller
public class ReserveController {

    private final ReservationService reservationService;

    public ReserveController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reserve")
    public String reserveForm(
            @RequestParam(required = false) Integer sid,
            @RequestParam(required = false) String date,
            Model model
    ) {
        model.addAttribute("sid", sid);
        model.addAttribute("date", date);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "reserve";
    }

    @PostMapping("/reserve")
    public String reserve(
            @RequestParam(required = false) Integer sid,
            @RequestParam(required = false) String date,
            HttpSession session
    ) {
        if (sid == null || date == null || date.isBlank()) {
            return "redirect:/calendar";
        }

        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            String loginUrl = UriComponentsBuilder
                    .fromPath("/login")
                    .queryParam("redirect", "reserve")
                    .queryParam("sid", sid)
                    .queryParam("date", date)
                    .build()
                    .toUriString();
            return "redirect:" + loginUrl;
        }

        final LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (Exception e) {
            return "redirect:/calendar";
        }

        try {
            reservationService.reserveSlot(sid, principal.getUserId(), d);
        } catch (RuntimeException ex) {
            String dayUrl = UriComponentsBuilder
                    .fromPath("/day")
                    .queryParam("date", date)
                    .queryParam("error", "reserve_failed")
                    .build()
                    .toUriString();
            return "redirect:" + dayUrl;
        }

        String okUrl = UriComponentsBuilder
                .fromPath("/day")
                .queryParam("date", date)
                .build()
                .toUriString();

        return "redirect:" + okUrl;
    }
}