package fr.but3.repository;

import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;

public class ReservationRepository {

    public int countPersonsInSlot(int sid) {

        EntityManager em = JPAUtil.getEM();

        try {
            Long result = em.createQuery(
                "SELECT COALESCE(SUM(r.nbPersonnes),0) FROM Reservation r WHERE r.slot.id = :sid",
                Long.class
            ).setParameter("sid", sid).getSingleResult();

            return result.intValue();

        } finally { em.close(); }
    }
}