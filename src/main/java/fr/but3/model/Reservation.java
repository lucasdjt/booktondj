package fr.but3.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "reservations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "sid"})
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rid")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sid", nullable = false)
    private Slot slot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @Column(name = "nb_personnes", nullable = false)
    private int nbPersonnes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Reservation() {}

    public Reservation(Slot slot, User user, int nbPersonnes) {
        this.slot = slot;
        this.user = user;
        this.nbPersonnes = nbPersonnes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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