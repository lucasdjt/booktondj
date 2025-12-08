package fr.but3.repository;

import fr.but3.model.Slot;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class SlotRepository {

    private final EntityManager em = JPAUtil.getEntityManager();

    public Slot find(int id) {
        return em.find(Slot.class, id);
    }

    public List<Slot> getSlotsForDay(LocalDate date) {
        return em.createQuery("""
            SELECT s FROM Slot s
            WHERE s.date = :d
            ORDER BY s.startTime
        """, Slot.class)
        .setParameter("d", date)
        .getResultList();
    }

    public void save(Slot s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
    }
}