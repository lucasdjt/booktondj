package fr.but3.controller;

import fr.but3.service.CalendarService;
import fr.but3.utils.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar")
    public String calendar(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model
    ) {
        CalendarService.CalendarResult r = calendarService.computeMonth(month, year);

        model.addAttribute("stats", r.stats());
        model.addAttribute("year", r.year());
        model.addAttribute("month", r.month());
        model.addAttribute("nbDays", r.nbDays());
        model.addAttribute("firstDay", r.firstDay());

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "calendar";
    }
}