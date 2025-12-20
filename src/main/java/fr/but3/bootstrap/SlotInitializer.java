package fr.but3.bootstrap;

import fr.but3.utils.SlotGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlotInitializer {

    @Bean
    CommandLineRunner initSlotsRunner(SlotGenerator generator) {
        return args -> generator.generateMissingSlots();
    }
}