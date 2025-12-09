package fr.but3.repository;

import fr.but3.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class UserRepository {

    private final EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public User find(int id) {
        return em.find(User.class, id);
    }

    public User findByName(String name) {
        TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.name = :n", User.class);
        q.setParameter("n", name);
        return q.getResultStream().findFirst().orElse(null);
    }

    public User save(User u) {
        em.getTransaction().begin();

        if (u.getId() == 0) {
            em.persist(u);
        } else {
            u = em.merge(u);
        }

        em.getTransaction().commit();
        return u;
    }
}