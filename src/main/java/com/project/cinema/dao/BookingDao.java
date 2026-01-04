package com.project.cinema.dao;

import com.project.cinema.model.Booking;
import com.project.cinema.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
    private final DatabaseConnection databaseConnection;

    public BookingDao(DatabaseConnection connection) {
        this.databaseConnection = connection;
    }

    private static final String INSERT_SQL = "INSERT INTO Booking (client_id, booking_code, created_at, status, payment_method, total_price) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Booking SET client_id=?, booking_code=?, created_at=?, status=?, payment_method=?, total_price=? WHERE bid=?";
    private static final String DELETE_SQL = "DELETE FROM Booking WHERE bid=?";
    private static final String SELECT_ALL_SQL = "SELECT bid, client_id, booking_code, created_at, status, payment_method, total_price FROM Booking";
    private static final String SELECT_BY_ID_SQL = "SELECT bid, client_id, booking_code, created_at, status, payment_method, total_price FROM Booking WHERE bid=?";

    Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    public void save(Booking booking) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, booking.getClientId());
            statement.setString(2, booking.getBookingCode());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getCreatedAt()));
            statement.setString(4, booking.getStatus());
            statement.setString(5, booking.getPaymentMethod());
            statement.setDouble(6, booking.getTotalPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBid(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Booking booking) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, booking.getClientId());
            statement.setString(2, booking.getBookingCode());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getCreatedAt()));
            statement.setString(4, booking.getStatus());
            statement.setString(5, booking.getPaymentMethod());
            statement.setDouble(6, booking.getTotalPrice());
            statement.setInt(7, booking.getBid());
            statement.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Booking> findAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        }
        return bookings;
    }

    public Booking findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        }
        return null;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("bid"),
                rs.getInt("client_id"),
                rs.getString("booking_code"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("status"),
                rs.getString("payment_method"),
                rs.getDouble("total_price"));
    }
}
