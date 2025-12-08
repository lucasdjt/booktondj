package fr.but3.listener;

import fr.but3.utils.SlotGenerator;
import jakarta.servlet.*;

public class SlotInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new SlotGenerator().generateMissingSlots();
    }
}