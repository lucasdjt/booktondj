package fr.but3;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class DayClickDAO {
    private Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = DayClickDAO.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new SQLException("Fichier de configuration introuvable");
            }
            props.load(input);
            Class.forName("org.postgresql.Driver");
            String url = props.getProperty("url");
            String user = props.getProperty("username");
            String password = props.getProperty("password");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Erreur de connexion à la base de données", e);
        }
    }
    public int getCounter(LocalDate date) {
        String sql = "SELECT counter FROM day_clicks WHERE day = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void increment(LocalDate date) {
        String sql = """
            INSERT INTO day_clicks(day, counter)
            VALUES (?, 1)
            ON CONFLICT(day)
            DO UPDATE SET counter = day_clicks.counter + 1
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
