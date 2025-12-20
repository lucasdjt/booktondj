package fr.but3.controller;

import fr.but3.model.Principal;
import fr.but3.model.Reservation;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.repository.UserRepository;
import fr.but3.utils.Config;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class ReserveController {

    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ReserveController(SlotRepository slotRepository,
                             UserRepository userRepository,
                             ReservationRepository reservationRepository) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reserve")
    public String reserveForm(@RequestParam(required = false) String sid,
                              @RequestParam(required = false) String date,
                              Model model) {

        model.addAttribute("sid", sid);
        model.addAttribute("date", date);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "reserve";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam(required = false) Integer sid,
                          @RequestParam(required = false) String date,
                          HttpSession session) {

        if (sid == null || date == null) {
            return "redirect:/calendar";
        }

        Principal principal = (Principal) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login?redirect=reserve&sid=" + sid + "&date=" + date;
        }

        LocalDate d = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        int maxDelay = Config.getMaxReservationDays();

        boolean limiteDepassee =
                d.isAfter(today.plusDays(maxDelay)) ||
                d.isBefore(today);

        if (limiteDepassee) {
            return "redirect:/day?date=" + date + "&error=expired";
        }

        Slot slot = slotRepository.findById(sid).orElse(null);
        if (slot == null) {
            return "redirect:/day?date=" + date + "&error=notslot";
        }

        User user = userRepository.findById(principal.getUserId()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        long used = reservationRepository.usedForSlot(sid);
        int remaining = slot.getCapacity() - (int) used;

        if (remaining <= 0) {
            return "redirect:/day?date=" + date + "&error=full";
        }

        long already = reservationRepository.countByUserIdAndSlotId(user.getId(), sid);
        if (already > 0) {
            return "redirect:/day?date=" + date + "&error=already";
        }

        reservationRepository.save(new Reservation(slot, user, 1));

        return "redirect:/day?date=" + date;
    }
}