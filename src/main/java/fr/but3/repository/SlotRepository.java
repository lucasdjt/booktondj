package fr.but3.repository;

import fr.but3.model.Slot;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class SlotRepository {

    public List<Slot> findByDate(LocalDate date) {
        EntityManager em = JPAUtil.getEM();
        try {
            return em.createQuery(
                    "SELECT s FROM Slot s WHERE s.date = :d ORDER BY s.startTime",
                    Slot.class
            ).setParameter("d", date).getResultList();

        } finally { em.close(); }
    }

    public Slot find(int id) {
        EntityManager em = JPAUtil.getEM();
        try { return em.find(Slot.class, id); }
        finally { em.close(); }
    }

    public void save(Slot s) {
        EntityManager em = JPAUtil.getEM();
        try {
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } finally { em.close(); }
    }
}
