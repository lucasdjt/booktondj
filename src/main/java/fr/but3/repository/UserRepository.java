package fr.but3.repository;

import fr.but3.model.User;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;

import java.util.List;

public class UserRepository {

    private final EntityManager em = JPAUtil.getEntityManager();

    public User findById(int id) {
        return em.find(User.class, id);
    }

    public User findByName(String name) {
        List<User> list = em.createQuery(
                "SELECT u FROM User u WHERE u.name = :n", User.class)
                .setParameter("n", name)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    public void save(User u) {
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
    }
}