package fr.but3.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sid")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "nb_personnes", nullable = false)
    private int nbPersonnes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Reservation() {}

    public Reservation(Slot slot, User user, int nb) {
        this.slot = slot;
        this.user = user;
        this.nbPersonnes = nb;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Slot getSlot() { return slot; }
    public User getUser() { return user; }
    public int getNbPersonnes() { return nbPersonnes; }
}