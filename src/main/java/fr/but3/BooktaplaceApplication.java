package fr.but3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "fr.but3")
public class BooktaplaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooktaplaceApplication.class, args);
    }
}