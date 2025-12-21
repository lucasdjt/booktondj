package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.service.ReservationService;
import fr.but3.utils.Config;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return UriComponentsBuilder
                    .fromPath("/login")
                    .queryParam("redirect", "reserve")
                    .queryParam("sid", sid)
                    .queryParam("date", date)
                    .build()
                    .toUriString();
        }

        final LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (Exception e) {
            return "redirect:/calendar";
        }

        try {
            reservationService.reserveSlot(sid, principal.getUserId(), d);
        } catch (ReservationService.ReservationException ex) {
            return UriComponentsBuilder
                    .fromPath("/day")
                    .queryParam("date", date)
                    .queryParam("error", ex.getCode())
                    .build()
                    .toUriString();
        }

        return UriComponentsBuilder
                .fromPath("/day")
                .queryParam("date", date)
                .build()
                .toUriString();
    }
}