package fr.but3.controller;

import fr.but3.model.JourStats;
import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
public class CalendarController {

    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public CalendarController(SlotRepository slotRepository,
                              ReservationRepository reservationRepository) {
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/calendar")
    public String calendar(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model
    ) {
        LocalDate today = LocalDate.now();

        int m = (month == null) ? today.getMonthValue() : month;
        int y = (year == null) ? today.getYear() : year;

        YearMonth ym = YearMonth.of(y, m);

        LocalDate start = ym.atDay(1);
        LocalDate end   = ym.atEndOfMonth();

        Set<Integer> joursActifs = Config.getEnabledDays();
        Set<LocalDate> joursFeries = Config.getHolidays(y);
        int maxDelay = Config.getMaxReservationDays();

        List<Slot> allSlots = slotRepository
                .findByDateBetweenOrderByDateAscStartTimeAsc(start, end);

        Map<LocalDate, List<Slot>> slotsByDate = new HashMap<>();
        for (Slot s : allSlots) {
            slotsByDate.computeIfAbsent(s.getDate(), d -> new ArrayList<>()).add(s);
        }

        List<Object[]> usedRows = reservationRepository.usedBySlotBetween(start, end);

        Map<Integer, Integer> usedBySlotId = new HashMap<>();
        for (Object[] row : usedRows) {
            Integer sid = ((Number) row[0]).intValue();
            Integer used = ((Number) row[1]).intValue();
            usedBySlotId.put(sid, used);
        }

        Map<Integer, JourStats> stats = new HashMap<>();

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = LocalDate.of(y, m, d);

            boolean ouvert = joursActifs.contains(date.getDayOfWeek().getValue());
            boolean ferie  = joursFeries.contains(date);

            boolean limiteDepassee =
                    date.isAfter(today.plusDays(maxDelay)) ||
                    date.isBefore(today);

            List<Slot> slots = slotsByDate.getOrDefault(date, Collections.emptyList());

            int dispo = 0;
            int totalPers = 0;
            int totalCap = 0;

            for (Slot s : slots) {
                int used = usedBySlotId.getOrDefault(s.getId(), 0);
                totalPers += used;
                totalCap  += s.getCapacity();
                if (used < s.getCapacity()) dispo++;
            }

            double taux = (totalCap == 0) ? 0.0 : (double) totalPers / totalCap;
            taux = Math.max(0, Math.min(1, taux));

            stats.put(d, new JourStats(dispo, totalPers, ouvert, ferie, taux, limiteDepassee));
        }

        int firstDayIndex = rotationIndex(Config.get("planning.premier_jour_semaine"));
        int firstDayMonth = rotateDay(ym.atDay(1).getDayOfWeek().getValue(), firstDayIndex);

        model.addAttribute("stats", stats);
        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("nbDays", ym.lengthOfMonth());
        model.addAttribute("firstDay", firstDayMonth);

        model.addAttribute("planningColorPrimary", Config.get("planning.couleur_principal"));
        model.addAttribute("planningColorSecondary", Config.get("planning.couleur_secondaire"));

        return "calendar";
    }

    private static int rotateDay(int dow, int startIndex) {
        int rotated = dow - startIndex + 1;
        if (rotated <= 0) rotated += 7;
        return rotated;
    }

    private static int rotationIndex(String raw) {
        if (raw == null) return 1;
        raw = raw.trim().toLowerCase();
        switch (raw) {
            case "lundi": return 1;
            case "mardi": return 2;
            case "mercredi": return 3;
            case "jeudi": return 4;
            case "vendredi": return 5;
            case "samedi": return 6;
            case "dimanche": return 7;
        }
        return 1;
    }
}