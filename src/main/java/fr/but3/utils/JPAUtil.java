package fr.but3.utils;

import jakarta.persistence.*;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("booktondjPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}