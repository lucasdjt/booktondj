package fr.but3.repository;

import fr.but3.model.Reservation;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;

public class ReservationRepository {

    private final EntityManager em = JPAUtil.getEntityManager();

    public int getUsedCapacityForSlot(int sid) {
        Long total = em.createQuery("""
            SELECT COALESCE(SUM(r.nbPersonnes),0)
            FROM Reservation r
            WHERE r.slot.id = :sid
        """, Long.class)
        .setParameter("sid", sid)
        .getSingleResult();

        return total.intValue();
    }

    public void save(Reservation r) {
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
    }
}