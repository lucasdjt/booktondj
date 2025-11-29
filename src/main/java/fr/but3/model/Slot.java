package fr.but3.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Slot {

    private int id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;

    public Slot(int id, LocalDate date, LocalTime startTime, LocalTime endTime, int capacity) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
