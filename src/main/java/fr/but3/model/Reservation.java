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

    @Column(name = "nb_personnes")
    private int nbPersonnes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Reservation() {}

    public Reservation(Slot slot, User user, int nbPersonnes) {
        this.slot = slot;
        this.user = user;
        this.nbPersonnes = nbPersonnes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
}