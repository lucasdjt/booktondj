package fr.but3.utils;

import fr.but3.model.Slot;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SlotGenerator {

    private final EntityManager em;

    public SlotGenerator(EntityManager em) {
        this.em = em;
    }

    public void generateMissingSlots() {

        LocalDate today = LocalDate.now();

        LocalDate firstReserved = em.createQuery(
                "SELECT MIN(r.slot.date) FROM Reservation r",
                LocalDate.class
        ).getSingleResult();

        LocalDate startDate =
                (firstReserved != null && firstReserved.isBefore(today))
                        ? firstReserved
                        : today;

        int maxDelay = Config.getMaxReservationDays();
        LocalDate endDate = today.plusDays(maxDelay);

        LocalTime startGlob = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime endGlob   = LocalTime.parse(Config.get("planning.horaire_final"));
        boolean overnight   = endGlob.isBefore(startGlob);

        boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));
        int duree         = fixedSlot
                ? Config.getMinutes("planning.creneau")
                : Config.getMinutes("planning.creneau_min");

        int pause         = fixedSlot ? 0 : Config.getMinutes("planning.pause_delai");
        int capacity      = Integer.parseInt(Config.get("planning.nb_prsn"));

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            LocalDateTime cursor = LocalDateTime.of(date, startGlob);
            LocalDateTime limit  = LocalDateTime.of(date, endGlob);
            if (overnight) limit = limit.plusDays(1);

            while (!cursor.isAfter(limit)) {

                LocalDateTime slotEnd = cursor.plusMinutes(duree);
                if (slotEnd.isAfter(limit.plusSeconds(1))) break;

                Long count = em.createQuery(
                        "SELECT COUNT(s) FROM Slot s " +
                        "WHERE s.date = :d AND s.startTime = :t",
                        Long.class
                )
                .setParameter("d", cursor.toLocalDate())
                .setParameter("t", cursor.toLocalTime())
                .getSingleResult();

                if (count == 0) {
                    em.getTransaction().begin();
                    em.persist(new Slot(
                            cursor.toLocalDate(),
                            cursor.toLocalTime(),
                            slotEnd.toLocalTime(),
                            capacity
                    ));
                    em.getTransaction().commit();
                }

                cursor = slotEnd.plusMinutes(pause);
            }
        }
    }
}