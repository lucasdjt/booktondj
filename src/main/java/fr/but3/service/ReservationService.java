package fr.but3.service;

import fr.but3.model.Reservation;
import fr.but3.model.Slot;
import fr.but3.model.User;
import fr.but3.repository.ReservationRepository;
import fr.but3.repository.SlotRepository;
import fr.but3.repository.UserRepository;
import fr.but3.utils.Config;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReservationService {

    public static class ReservationException extends RuntimeException {
        private final String code;

        public ReservationException(String code) {
            super(code);
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            SlotRepository slotRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void reserveSlot(Integer slotId, Integer userId, LocalDate date) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ReservationException("notslot"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ReservationException("user"));

        LocalDate today = LocalDate.now();
        int maxDelay = Config.getMaxReservationDays();

        if (date.isBefore(today) || date.isAfter(today.plusDays(maxDelay))) {
            throw new ReservationException("expired");
        }

        long used = reservationRepository.usedForSlot(slotId);
        if (used >= slot.getCapacity()) {
            throw new ReservationException("full");
        }

        long already = reservationRepository.countByUserIdAndSlotId(user.getId(), slotId);
        if (already > 0) {
            throw new ReservationException("already");
        }

        reservationRepository.save(new Reservation(slot, user, 1));
    }
}