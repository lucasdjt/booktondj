package fr.but3.service;

import fr.but3.model.JourStats;
import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class CalendarService {

    public record CalendarResult(
            Map<Integer, JourStats> stats,
            int year,
            int month,
            int nbDays,
            int firstDay
    ) {}

    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public CalendarService(SlotRepository slotRepository,
                           ReservationRepository reservationRepository) {
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    public CalendarResult computeMonth(Integer month, Integer year) {

        LocalDate today = LocalDate.now();
        int m = (month == null) ? today.getMonthValue() : month;
        int y = (year == null) ? today.getYear() : year;

        YearMonth ym = YearMonth.of(y, m);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

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
            boolean ferie = joursFeries.contains(date);

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
                totalCap += s.getCapacity();
                if (used < s.getCapacity()) dispo++;
            }

            double taux = (totalCap == 0) ? 0.0 : (double) totalPers / totalCap;
            taux = Math.max(0, Math.min(1, taux));

            stats.put(d, new JourStats(dispo, totalPers, ouvert, ferie, taux, limiteDepassee));
        }

        int firstDayIndex = rotationIndex(Config.get("planning.premier_jour_semaine"));
        int firstDayMonth = rotateDay(ym.atDay(1).getDayOfWeek().getValue(), firstDayIndex);

        return new CalendarResult(stats, y, m, ym.lengthOfMonth(), firstDayMonth);
    }

    private static int rotateDay(int dow, int startIndex) {
        int rotated = dow - startIndex + 1;
        if (rotated <= 0) rotated += 7;
        return rotated;
    }

    private static int rotationIndex(String raw) {
        if (raw == null) return 1;
        raw = raw.trim().toLowerCase();
        return switch (raw) {
            case "lundi" -> 1;
            case "mardi" -> 2;
            case "mercredi" -> 3;
            case "jeudi" -> 4;
            case "vendredi" -> 5;
            case "samedi" -> 6;
            case "dimanche" -> 7;
            default -> 1;
        };
    }
}