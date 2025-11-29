package fr.but3.dao;

import fr.but3.model.User;

import java.sql.*;

public class UserDAO {

    public User findByName(String name) {
        String sql = "SELECT uid, name FROM users WHERE name = ?";
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("uid"),
                            rs.getString("name")
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User create(String name) {
        String sql = "INSERT INTO users(name) VALUES (?) RETURNING uid";
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("uid");
                    return new User(id, name);
                } else {
                    throw new RuntimeException("INSERT user sans retour d'ID");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User findOrCreateByName(String name) {
        User u = findByName(name);
        if (u != null) return u;
        return create(name);
    }
}
