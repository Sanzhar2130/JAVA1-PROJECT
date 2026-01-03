package com.example.jp_1.dao;


import com.example.jp_1.model.Hall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HallDao {
    private final DatabaseConnection databaseConnection;

    public HallDao(DatabaseConnection connection) {
        this.databaseConnection = connection;
    }

    private static final String INSERT_SQL = "INSERT INTO Hall (name, screen_type) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE Hall SET name=?, screen_type=? WHERE hid=?";
    private static final String DELETE_SQL = "DELETE FROM Hall WHERE hid=?";
    private static final String SELECT_ALL_SQL = "SELECT hid, name, screen_type FROM Hall";
    private static final String SELECT_BY_ID_SQL = "SELECT hid, name, screen_type FROM Hall WHERE hid=?";

    Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    public void save(Hall hall) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, hall.getName());
            statement.setString(2, hall.getScreenType());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating hall failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hall.setHid(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Hall hall) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, hall.getName());
            statement.setString(2, hall.getScreenType());
            statement.setInt(3, hall.getHid());
            statement.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Hall> findAll() throws SQLException {
        List<Hall> halls = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                halls.add(new Hall(
                        rs.getInt("hid"),
                        rs.getString("name"),
                        rs.getString("screen_type")));
            }
        }
        return halls;
    }

    public Hall findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    return new Hall(
                            rs.getInt("hid"),
                            rs.getString("name"),
                            rs.getString("screen_type"));
                }
            }
        }
        return null;
    }
}
