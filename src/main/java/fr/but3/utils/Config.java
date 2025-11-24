package fr.but3.utils;

import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private static final Properties props = new Properties();

    static {
        try {
            Properties mainConfig = new Properties();
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (in == null) {
                    throw new RuntimeException("Impossible de trouver config.properties");
                }
                mainConfig.load(in);
            }

            String activeFile = mainConfig.getProperty("properties");
            if (activeFile == null || activeFile.trim().isEmpty()) {
                throw new RuntimeException("properties non défini dans config.properties");
            }

            try (InputStream in = Config.class.getClassLoader().getResourceAsStream(activeFile)) {
                if (in == null) {
                    throw new RuntimeException("Impossible de charger " + activeFile);
                }
                props.load(in);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement des fichiers de configuration", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getMinutes(String key) {
        String v = props.getProperty(key);
        if (v == null) return 0;
        v = v.trim();
        if (v.endsWith("m")) return Integer.parseInt(v.substring(0, v.length() - 1));
        if (v.endsWith("h")) return Integer.parseInt(v.substring(0, v.length() - 1)) * 60;
        return Integer.parseInt(v);
    }

    public static Set<Integer> getEnabledDays() {
        String v = props.getProperty("planning.jours_disponibles", "all").trim().toLowerCase();
        Set<Integer> set = new HashSet<>();

        if (v.equals("all")) {
            for (int i = 1; i <= 7; i++) set.add(i);
            return set;
        }

        for (char c : v.toCharArray()) {
            if (c >= '1' && c <= '7') set.add(c - '0');
        }

        return set;
    }

    public static Set<LocalDate> getHolidays(int year) {
        Set<LocalDate> result = new HashSet<>();
        parseHolidayList(props.getProperty("planning.ferie"), year, result);
        parseHolidayList(props.getProperty("planning.ferie_" + year), year, result);
        return result;
    }

    private static void parseHolidayList(String raw, int year, Set<LocalDate> target) {
        if (raw == null) return;

        raw = raw.trim();
        if (raw.startsWith("[")) raw = raw.substring(1);
        if (raw.endsWith("]")) raw = raw.substring(0, raw.length() - 1);
        if (raw.isEmpty()) return;

        String[] entries = raw.split(",");
        for (String e : entries) {
            String[] kv = e.split("=");
            if (kv.length != 2) continue;
            String date = kv[1];

            String[] dm = date.split("-");
            if (dm.length != 2) continue;

            try {
                target.add(LocalDate.of(year,
                        Integer.parseInt(dm[1]),
                        Integer.parseInt(dm[0])
                ));
            } catch (Exception ignored) {}
        }
    }

    public static int getMaxReservationDays() {
        String raw = props.getProperty("planning.delai_max_reservation", "0");
        raw = raw.trim().toLowerCase();

        if (raw.endsWith("j")) {
            return Integer.parseInt(raw.substring(0, raw.length() - 1));
        }

        return Integer.parseInt(raw);
    }
}
