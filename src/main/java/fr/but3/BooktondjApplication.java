package fr.but3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "fr.but3")
public class BooktondjApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooktondjApplication.class, args);
    }
}