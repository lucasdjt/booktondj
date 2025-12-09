package fr.but3.repository;

import fr.but3.model.Slot;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

public class SlotRepository {

    private final EntityManager em;

    public SlotRepository(EntityManager em) {
        this.em = em;
    }

    public Slot find(int id) {
        return em.find(Slot.class, id);
    }

    public List<Slot> findByDate(LocalDate d) {
        return em.createQuery(
                "SELECT s FROM Slot s WHERE s.date = :d ORDER BY s.startTime",
                Slot.class
        ).setParameter("d", d).getResultList();
    }

    public void save(Slot s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
    }
}