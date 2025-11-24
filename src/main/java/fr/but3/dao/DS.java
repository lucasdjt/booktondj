package fr.but3.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DS {

    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Properties props = new Properties();

            try (InputStream input = DS.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {

                if (input == null) {
                    throw new RuntimeException("config.properties introuvable");
                }

                props.load(input);

                url = props.getProperty("url");
                username = props.getProperty("username");
                password = props.getProperty("password");

                Class.forName("org.postgresql.Driver");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur d'initialisation du DS", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'obtenir une connexion", e);
        }
    }
}