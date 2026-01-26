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
        public String getCode() { return code; }
    }

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public ReservationService(ReservationRepository reservationRepository,
                              SlotRepository slotRepository,
                              UserRepository userRepository,
                              MailService mailService) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
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

        long already = reservationRepository.countByUserIdAndSlotId(userId, slotId);
        if (already > 0) {
            throw new ReservationException("already");
        }

        reservationRepository.save(new Reservation(slot, user, 1));

        String subject = "Confirmation de réservation - BookTaPlace";
        String body =
                "Bonjour " + user.getName() + ",\n\n" +
                "Votre réservation est confirmée.\n" +
                "Date : " + slot.getDate() + "\n" +
                "Créneau : " + slot.getStartTime() + " - " + slot.getEndTime() + "\n\n" +
                "À bientôt.";
        mailService.sendSafe(user.getEmail(), subject, body);
    }

    @Transactional
    public void cancelReservation(Integer reservationId, Integer requesterUserId, boolean isAdmin) {

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("notfound"));

        if (r.getUser() == null || r.getUser().getId() == null) {
            throw new ReservationException("forbidden");
        }

        int ownerId = r.getUser().getId();
        if (!isAdmin && ownerId != requesterUserId) {
            throw new ReservationException("forbidden");
        }

        User user = r.getUser();
        Slot slot = r.getSlot();

        reservationRepository.delete(r);

        String subject = "Annulation de réservation - BookTaPlace";
        String body =
                "Bonjour " + user.getName() + ",\n\n" +
                "Votre réservation a bien été annulée.\n" +
                "Date : " + (slot != null ? slot.getDate() : "?") + "\n" +
                "Créneau : " + (slot != null ? (slot.getStartTime() + " - " + slot.getEndTime()) : "?") + "\n\n" +
                "À bientôt.";
        mailService.sendSafe(user.getEmail(), subject, body);
    }
}