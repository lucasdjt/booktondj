package fr.but3.utils;

import fr.but3.dao.SlotDAO;
import fr.but3.dao.ReservationDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SlotGenerator {

    private final SlotDAO slotDAO = new SlotDAO();
    private final ReservationDAO resDAO = new ReservationDAO();

    public void generateMissingSlots() {

        LocalDate today = LocalDate.now();
        LocalDate firstReserved = resDAO.getOldestReservationDate();

        LocalDate startDate = (firstReserved != null && firstReserved.isBefore(today))
                ? firstReserved
                : today;

        int maxDelay = Config.getMaxReservationDays();
        LocalDate endDate = today.plusDays(maxDelay);

        LocalTime startGlob = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime endGlob   = LocalTime.parse(Config.get("planning.horaire_final"));

        boolean overnight = endGlob.isBefore(startGlob);

        boolean fixedSlot = "oui".equalsIgnoreCase(Config.get("planning.creneau_par_temps"));
        int duree = fixedSlot
                ? Config.getMinutes("planning.creneau")
                : Config.getMinutes("planning.creneau_min");

        int pause = fixedSlot ? 0 : Config.getMinutes("planning.pause_delai");
        int capacity = Integer.parseInt(Config.get("planning.nb_prsn"));

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            LocalDateTime cursor = LocalDateTime.of(date, startGlob);
            LocalDateTime limit = LocalDateTime.of(date, endGlob);
            if (overnight) limit = limit.plusDays(1);

            while (!cursor.isAfter(limit)) {

                LocalDateTime slotEnd = cursor.plusMinutes(duree);
                if (slotEnd.isAfter(limit.plusSeconds(1))) break;

                slotDAO.insertSlotIfNotExists(
                        cursor.toLocalDate(),
                        cursor.toLocalTime(),
                        slotEnd.toLocalTime(),
                        capacity
                );

                cursor = slotEnd.plusMinutes(pause);
            }
        }
    }
}