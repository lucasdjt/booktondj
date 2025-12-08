package fr.but3.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sid")
    private int id;

    @Column(name = "slot_date", nullable = false)
    private LocalDate date;

    @Column(name = "time_start", nullable = false)
    private LocalTime startTime;

    @Column(name = "time_end", nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int capacity;

    public Slot() {}

    public Slot(LocalDate d, LocalTime s, LocalTime e, int cap) {
        this.date = d;
        this.startTime = s;
        this.endTime = e;
        this.capacity = cap;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getCapacity() { return capacity; }
}