package com.example.jp_1.dao;

import com.example.jp_1.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao extends GenericDao<Booking, Integer> {
    public BookingDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO Booking (client_id, bookingCode, createdAt, status, paymentMethod, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Booking SET client_id=?, bookingCode=?, createdAt=?, status=?, paymentMethod=?, totalPrice=? WHERE bid=?";
    private static final String DELETE_SQL = "DELETE FROM Booking WHERE bid=?";
    private static final String SELECT_ALL_SQL = "SELECT bid, client_id, bookingCode, createdAt, status, paymentMethod, totalPrice FROM Booking";
    private static final String SELECT_BY_ID_SQL = "SELECT bid, client_id, bookingCode, createdAt, status, paymentMethod, totalPrice FROM Booking WHERE bid=?";

    @Override
    public void save(Booking booking) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL)) {
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

    @Override
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

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Booking> findAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("bid"),
                        rs.getInt("client_id"),
                        rs.getString("bookingCode"),
                        rs.getTimestamp("createdAt").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("totalPrice")));
            }
        }
        return bookings;
    }

    @Override
    public Booking findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    return new Booking(
                            rs.getInt("bid"),
                            rs.getInt("clientId"),
                            rs.getString("bookingCode"),
                            rs.getTimestamp("createdAt").toLocalDateTime(),
                            rs.getString("status"),
                            rs.getString("paymentMethod"),
                            rs.getDouble("totalPrice"));
                }
            }
        }
        return null;
    }
}
