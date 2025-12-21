package fr.but3.controller;

import fr.but3.service.DayService;
import fr.but3.utils.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class DayController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @GetMapping("/day")
    public String day(@RequestParam(required = false) String date,
                      @RequestParam(required = false, defaultValue = "creneaux") String mode,
                      Model model) {

        if (date == null || date.isBlank()) {
            return "redirect:/calendar";
        }

        final LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (Exception e) {
            return "redirect:/calendar";
        }

        model.addAttribute("date", d);
        model.addAttribute("mode", (mode == null || mode.isBlank()) ? "creneaux" : mode);
        model.addAttribute("creneaux", dayService.buildCreneaux(d));

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "day";
    }
}