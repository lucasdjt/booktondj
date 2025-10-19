package fr.but3;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello !");
        System.out.println("Date actuelle : " + LocalDate.now());
        System.out.println("Obtenir 1er jour du mois : " + LocalDate.now().withDayOfMonth(1).getDayOfWeek().getValue()); // 1 Octobre = Mercredi
        System.out.println("Nombre de jours du mois : " + LocalDate.now().lengthOfMonth()); // 31
    }
}