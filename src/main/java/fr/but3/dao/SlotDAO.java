package fr.but3.dao;

import fr.but3.model.Slot;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SlotDAO {

    public LocalDate getLastSlotDate() {
        String sql = "SELECT MAX(slot_date) FROM slots";

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Date d = rs.getDate(1);
                return (d == null) ? null : d.toLocalDate();
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Erreur getLastSlotDate()", e);
        }
    }

    public void insertSlotIfNotExists(LocalDate date, LocalTime start, LocalTime end, int cap) {
        String sql = """
            INSERT INTO slots(slot_date, time_start, time_end, capacity)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (slot_date, time_start) DO NOTHING
        """;

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(start));
            ps.setTime(3, Time.valueOf(end));
            ps.setInt(4, cap);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Erreur insertSlotIfNotExists()", e);
        }
    }

    public List<Slot> getSlotsForDay(LocalDate date) {
        String sql = """
            SELECT sid, slot_date, time_start, time_end, capacity
            FROM slots
            WHERE slot_date = ?
            ORDER BY time_start
        """;

        List<Slot> list = new ArrayList<>();

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Slot(
                        rs.getInt("sid"),
                        rs.getDate("slot_date").toLocalDate(),
                        rs.getTime("time_start").toLocalTime(),
                        rs.getTime("time_end").toLocalTime(),
                        rs.getInt("capacity")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur getSlotsForDay()", e);
        }
        return list;
    }

    public Slot getById(int sid) {
        String sql = "SELECT * FROM slots WHERE sid = ?";

        try (Connection c = DS.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Slot(
                        rs.getInt("sid"),
                        rs.getDate("slot_date").toLocalDate(),
                        rs.getTime("time_start").toLocalTime(),
                        rs.getTime("time_end").toLocalTime(),
                        rs.getInt("capacity")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur getById()", e);
        }

        return null;
    }
}