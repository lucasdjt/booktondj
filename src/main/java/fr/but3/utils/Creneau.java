package fr.but3.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class Creneau {
    public LocalTime timeStart;
    public LocalTime timeEnd;
    public int nbInscrits = 0;
    public int maxPers = 0;
    public boolean complet = false;
    public LocalDate dateReelle;
    public boolean reservable = true;

    public Creneau(LocalTime start, LocalTime end) {
        this.timeStart = start;
        this.timeEnd = end;
    }

    public Creneau(LocalTime start, LocalTime end, LocalDate realDate) {
        this.timeStart = start;
        this.timeEnd = end;
        this.dateReelle = realDate;
    }
}
