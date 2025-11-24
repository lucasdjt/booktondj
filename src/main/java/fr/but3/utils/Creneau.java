package fr.but3.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class Creneau {
    public LocalTime timeStart;
    public LocalTime timeEnd;
    public int nbInscrits;
    public int maxPers;
    public boolean complet;
    public LocalDate dateReelle;

    public Creneau(LocalTime s, LocalTime e) {
        this.timeStart = s;
        this.timeEnd = e;
    }
}
