package fr.but3.listener;

import fr.but3.utils.Config;
import fr.but3.utils.SlotGenerator;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.time.LocalDate;

@WebListener
public class SlotInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("=== INITIALISATION DES CRENEAUX ===");
        SlotGenerator generator = new SlotGenerator();

        int maxDays = Config.getMaxReservationDays();

        LocalDate today = LocalDate.now();
        LocalDate horizon = today.plusDays(maxDays);

        generator.generateMissingSlots();

        System.out.println("Créneaux générés jusqu’à : " + horizon);
        System.out.println("=====================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
