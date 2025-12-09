package fr.but3.repository;

import fr.but3.model.Reservation;
import jakarta.persistence.*;

public class ReservationRepository {

    private final EntityManager em;

    public ReservationRepository(EntityManager em) {
        this.em = em;
    }

    public int countForSlot(int slotId) {
        Long count = em.createQuery(
                "SELECT COALESCE(SUM(r.nbPersonnes),0) FROM Reservation r WHERE r.slot.id = :sid",
                Long.class
        ).setParameter("sid", slotId).getSingleResult();

        return count.intValue();
    }

    public void save(Reservation r) {
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
    }
}
