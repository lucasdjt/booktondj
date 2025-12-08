package fr.but3.repository;

import fr.but3.model.User;
import fr.but3.utils.JPAUtil;
import jakarta.persistence.*;

public class UserRepository {

    public User findByName(String name) {
        EntityManager em = JPAUtil.getEM();

        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name = :n", User.class)
                     .setParameter("n", name)
                     .getResultStream()
                     .findFirst()
                     .orElse(null);
        } finally {
            em.close();
        }
    }

    public User save(User u) {
        EntityManager em = JPAUtil.getEM();

        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            return u;
        } finally {
            em.close();
        }
    }

    public User findOrCreate(String name) {
        User u = findByName(name);
        if (u != null) return u;
        return save(new User(name));
    }
}
