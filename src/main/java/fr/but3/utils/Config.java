package fr.but3.utils;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("coiffeur.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
