package fr.but3.utils;

import fr.but3.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PlanningUtils {

    public static List<Creneau> buildCreneauxForDay(LocalDate date, List<Booking> bookings) {

        LocalTime start = LocalTime.parse(Config.get("planning.horaire_debut"));
        LocalTime end   = LocalTime.parse(Config.get("planning.horaire_final"));

        int maxPers = Integer.parseInt(Config.get("planning.nb_prsn"));

        String parTemps = Config.get("planning.creneau_par_temps");
        int duree;
        if ("oui".equalsIgnoreCase(parTemps)) {
            duree = Config.getMinutes("planning.creneau");
        } else {
            duree = Config.getMinutes("planning.creneau_min");
        }

        int pause = 0;
        String pauseStr = Config.get("planning.pause_delai");
        if (pauseStr != null && !pauseStr.isBlank()) {
            pause = Config.getMinutes("planning.pause_delai");
        }

        List<Creneau> result = new ArrayList<>();

        int startMin = start.getHour() * 60 + start.getMinute();
        int endMin = end.getHour() * 60 + end.getMinute();

        if (endMin <= startMin) {
            endMin += 24 * 60;
        }

        int current = startMin;

        while (current < endMin) {
            int next = current + duree;

            LocalTime tStart = LocalTime.ofSecondOfDay((current % (24 * 60)) * 60L);
            LocalTime tEnd = LocalTime.ofSecondOfDay((next % (24 * 60)) * 60L);

            Creneau c = new Creneau(tStart, tEnd);

            int total = 0;
            for (Booking b : bookings) {
                if (b.getTimeStart().equals(tStart)) {
                    total += b.getNbPersonnes();
                }
            }

            c.nbInscrits = total;
            c.maxPers = maxPers;
            c.complet = total >= maxPers;

            result.add(c);

            current = next + pause;
        }

        return result;
    }
}
