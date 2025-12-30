package com.example.jp_1.dao;

import com.example.jp_1.model.TicketType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketTypeDao extends GenericDao<TicketType, Integer> {
    public TicketTypeDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO TicketType (name, discount_percent) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE TicketType SET name=?, discount_percent=? WHERE ttid=?";
    private static final String DELETE_SQL = "DELETE FROM TicketType WHERE ttid=?";
    private static final String SELECT_ALL_SQL = "SELECT ttid, name, discount_percent FROM TicketType";
    private static final String SELECT_BY_ID_SQL = "SELECT ttid, name, discount_percent FROM TicketType WHERE ttid=?";

    @Override
    public void save(TicketType type) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, type.getName());
            statement.setDouble(2, type.getDiscountPercent());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ticket type failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    type.setTtId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(TicketType type) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, type.getName());
            statement.setDouble(2, type.getDiscountPercent());
            statement.setInt(3, type.getTtId());
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
    public List<TicketType> findAll() throws SQLException {
        List<TicketType> types = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_ALL_SQL);
            while (rs.next()) {
                types.add(new TicketType(rs.getInt("ttid"), rs.getString("name"), rs.getDouble("discount_percent")));
            }
        }
        return types;
    }

    @Override
    public TicketType findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new TicketType(rs.getInt("ttid"), rs.getString("name"), rs.getDouble("discount_percent"));
                }
            }
        }
        return null;
    }
}
