package fr.but3.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {

    private int id;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String customerName;
    private int nbPersonnes;

    public Booking(int id, LocalDate date, LocalTime timeStart, LocalTime timeEnd,
                   String customerName, int nbPersonnes) {
        this.id = id;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.customerName = customerName;
        this.nbPersonnes = nbPersonnes;
    }

    public Booking(LocalDate date, LocalTime timeStart, LocalTime timeEnd,
                   String customerName, int nbPersonnes) {
        this(0, date, timeStart, timeEnd, customerName, nbPersonnes);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }
}
