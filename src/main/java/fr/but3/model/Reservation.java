package fr.but3.model;

import java.time.LocalDateTime;

public class Reservation {

    private int id;
    private int slotId;
    private int userId;
    private int nbPersonnes;
    private LocalDateTime createdAt;

    public Reservation(int id, int slotId, int userId, int nbPersonnes, LocalDateTime createdAt) {
        this.id = id;
        this.slotId = slotId;
        this.userId = userId;
        this.nbPersonnes = nbPersonnes;
        this.createdAt = createdAt;
    }

    public Reservation(int slotId, int userId, int nbPersonnes) {
        this(0, slotId, userId, nbPersonnes, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public int getSlotId() {
        return slotId;
    }

    public int getUserId() {
        return userId;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }
}
