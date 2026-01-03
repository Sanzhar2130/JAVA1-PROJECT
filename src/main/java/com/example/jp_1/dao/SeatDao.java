package com.example.jp_1.dao;

import com.example.jp_1.model.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {
    private final DatabaseConnection databaseConnection;

    public SeatDao(DatabaseConnection connection) {
        this.databaseConnection = connection;
    }


    private static final String INSERT_SQL = "INSERT INTO Seat (hid, row_num, seat_num) VALUES (?,?,?)";
    private static final String UPDATE_SQL = "UPDATE Seat SET hid=?, row_num=?, seat_num=? WHERE sid=?";
    private static final String DELETE_SQL = "DELETE FROM Seat WHERE sid=?";
    private static final String SELECT_ALL_SQL = "SELECT sid, hid, row_num, seat_num FROM Seat";
    private static final String SELECT_BY_ID_SQL = "SELECT sid, hid, row_num, seat_num FROM Seat WHERE sid=?";

    Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    public void save(Seat seat) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL)) {
            statement.setInt(1, seat.getHid());
            statement.setInt(2, seat.getRowNumber());
            statement.setInt(3, seat.getSeatNumber());
            statement.setBoolean(4, seat.getIsVip());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating seat failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    seat.setSid(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Seat seat) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, seat.getHid());
            statement.setInt(2, seat.getRowNumber());
            statement.setInt(3, seat.getSeatNumber());
            statement.setInt(4, seat.getSid());
            statement.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Seat> findAll() throws SQLException {
        List<Seat> seats = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
             while(rs.next()) {
                 seats.add(mapResultSetToSeat(rs));
             }
        }
        return seats;
    }

    public Seat findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        }
        return null;
    }

    public List<Seat> findByHid(int hid) throws SQLException {
        String sql = "SELECT * FROM Seat WHERE hid = ?";
        List<Seat> seats = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, hid);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }
        }
        return seats;
    }

    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        return new Seat(
                rs.getInt("sid"),
                rs.getInt("hid"),
                rs.getInt("row_num"),
                rs.getInt("seat_num"),
                rs.getBoolean("is_vip"));
    }
}
