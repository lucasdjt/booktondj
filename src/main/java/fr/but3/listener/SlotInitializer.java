package fr.but3.listener;

import fr.but3.utils.JPAUtil;
import fr.but3.utils.SlotGenerator;
import jakarta.persistence.EntityManager;
import jakarta.servlet.*;

public class SlotInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            SlotGenerator gen = new SlotGenerator(em);
            gen.generateMissingSlots();
        } finally {
            em.close();
        }
    }
}