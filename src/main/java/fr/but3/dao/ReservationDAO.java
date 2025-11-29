package fr.but3.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReservationDAO {

    public void createReservation(int slotId, int userId, int nbPersonnes) {
        String sql = """
            INSERT INTO reservations(sid, uid, nb_personnes)
            VALUES (?, ?, ?)
        """;

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotId);
            ps.setInt(2, userId);
            ps.setInt(3, nbPersonnes);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDate getOldestReservationDate() {
        String sql = """
            SELECT MIN(s.slot_date) AS d
            FROM reservations r
            JOIN slots s ON r.sid = s.sid
        """;

        try (Connection c = DS.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Date d = rs.getDate("d");
                return (d != null) ? d.toLocalDate() : null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public int getUsedCapacityForSlot(int slotId) {
        String sql = "SELECT COALESCE(SUM(nb_personnes), 0) AS used FROM reservations WHERE sid = ?";
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("used");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    
    public int countPersonsForSlot(int sid) {
        String sql = "SELECT COALESCE(SUM(nb_personnes), 0) AS used FROM reservations WHERE sid = ?";

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("used");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    public Map<Integer, Integer> getUsedCapacityForMonth(int year, int month) {
        String sql = """
            SELECT r.sid, SUM(r.nb_personnes) AS used
            FROM reservations r
            JOIN slots s ON r.sid = s.sid
            WHERE EXTRACT(YEAR FROM s.slot_date) = ?
            AND EXTRACT(MONTH FROM s.slot_date) = ?
            GROUP BY r.sid
        """;

        Map<Integer, Integer> map = new HashMap<>();

        try (Connection c = DS.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("sid"), rs.getInt("used"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public Map<Integer, Integer> getUsedCapacityForDay(LocalDate date) {
        String sql = """
            SELECT r.sid, SUM(r.nb_personnes) AS used
            FROM reservations r
            JOIN slots s ON r.sid = s.sid
            WHERE s.slot_date = ?
            GROUP BY r.sid
        """;

        Map<Integer, Integer> map = new HashMap<>();

        try (Connection c = DS.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("sid"), rs.getInt("used"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return map;
    }

}