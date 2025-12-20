package fr.but3.controller.admin;

import fr.but3.model.Slot;
import fr.but3.repository.SlotRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminSlotsController {

    private final SlotRepository slotRepository;

    public AdminSlotsController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @GetMapping("/admin/slots")
    public String slots(Model model) {
        model.addAttribute("slots", slotRepository.findAll(Sort.by("date").ascending().and(Sort.by("startTime").ascending())));
        return "admin/slots";
    }

    @PostMapping("/admin/slots")
    public String slotsAction(@RequestParam String action,
                              @RequestParam(required = false) Integer sid,
                              @RequestParam(required = false) String date) {

        switch (action) {

            case "delete-slot":
                if (sid != null) slotRepository.deleteById(sid);
                break;

            case "delete-day":
                if (date != null) {
                    LocalDate d = LocalDate.parse(date);
                    List<Slot> slots = slotRepository.findByDateOrderByStartTime(d);
                    slotRepository.deleteAll(slots);
                }
                break;
        }

        return "redirect:/admin/slots";
    }
}