package fr.but3.controller;

import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
public class DayController {

    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public DayController(SlotRepository slotRepository,
                         ReservationRepository reservationRepository) {
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/day")
    public String day(@RequestParam(required = false) String date,
                      @RequestParam(required = false, defaultValue = "creneaux") String mode,
                      Model model) {

        if (date == null) {
            return "redirect:/calendar";
        }

        LocalDate d = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        int maxDelay = Config.getMaxReservationDays();

        List<Slot> slots = slotRepository.findByDateOrderByStartTime(d);

        List<Object[]> usedRows = reservationRepository.usedBySlotForDate(d);
        Map<Integer, Integer> usedBySlotId = new HashMap<>();
        for (Object[] row : usedRows) {
            Integer sid = ((Number) row[0]).intValue();
            Integer used = ((Number) row[1]).intValue();
            usedBySlotId.put(sid, used);
        }

        Set<Integer> joursActifs = Config.getEnabledDays();
        boolean ouvert = joursActifs.contains(d.getDayOfWeek().getValue());

        Set<LocalDate> holidays = Config.getHolidays(d.getYear());
        boolean ferie = holidays.contains(d);

        boolean limiteDepassee =
                d.isAfter(today.plusDays(maxDelay)) ||
                d.isBefore(today);

        List<Map<String, Object>> creneaux = new ArrayList<>();
        for (Slot s : slots) {
            int used = usedBySlotId.getOrDefault(s.getId(), 0);
            boolean complet = used >= s.getCapacity();

            boolean reservable =
                    !complet &&
                    !limiteDepassee &&
                    ouvert &&
                    !ferie;

            Map<String, Object> m = new HashMap<>();
            m.put("slot", s);
            m.put("used", used);
            m.put("complet", complet);
            m.put("reservable", reservable);
            creneaux.add(m);
        }

        Map<Integer, List<Map<String, Object>>> hours = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) hours.put(h, new ArrayList<>());

        for (Map<String, Object> entry : creneaux) {
            Slot s = (Slot) entry.get("slot");
            hours.get(s.getStartTime().getHour()).add(entry);
        }

        model.addAttribute("creneaux", creneaux);
        model.addAttribute("hours", hours);
        model.addAttribute("mode", mode);
        model.addAttribute("date", d);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "day";
    }
}
