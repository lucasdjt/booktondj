package fr.but3.service;

import fr.but3.model.Slot;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.utils.Config;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DayService {

    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;

    public DayService(SlotRepository slotRepository,
                      ReservationRepository reservationRepository) {
        this.slotRepository = slotRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Map<String, Object>> buildCreneaux(LocalDate d) {

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

        return creneaux;
    }
}