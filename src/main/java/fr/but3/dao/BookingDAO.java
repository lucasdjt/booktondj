package fr.but3.dao;

import fr.but3.model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public List<Booking> getBookingsForDay(LocalDate date) {
        String sql = "SELECT * FROM bookings WHERE date = ?";
        List<Booking> list = new ArrayList<>();

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Booking(
                        rs.getInt("bid"),
                        date,
                        rs.getTime("time_start").toLocalTime(),
                        rs.getTime("time_end").toLocalTime(),
                        rs.getString("customer_name"),
                        rs.getInt("nb_personnes")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void createBooking(LocalDate date, LocalTime timeStart, LocalTime timeEnd,
                              String name, int nb) {

        String sql = """
            INSERT INTO bookings(date, time_start, time_end, customer_name, nb_personnes)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(timeStart));
            ps.setTime(3, Time.valueOf(timeEnd));
            ps.setString(4, name);
            ps.setInt(5, nb);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}