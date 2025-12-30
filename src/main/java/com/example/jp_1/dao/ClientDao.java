package com.example.jp_1.dao;


import com.example.jp_1.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends GenericDao<Client, Integer> {
    public ClientDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO Client (fname, lname, email, phone) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Client SET fname=?, lname=?, email=?, phone=? WHERE client_id=?";
    private static final String DELETE_SQL = "DELETE FROM Client WHERE client_id=?";
    private static final String SELECT_ALL_SQL = "SELECT client_id, fname, lname, email, phone FROM Client";
    private static final String SELECT_BY_ID_SQL = "SELECT client_id, fname, lname, email, phone FROM Client WHERE client_id=?";

    @Override
    public void save(Client client) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setClientId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Client client) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setInt(5, client.getClientId());
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
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    @Override
    public Client findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("client_id"),
                rs.getString("fname"),
                rs.getString("lname"),
                rs.getString("email"),
                rs.getString("phone"));
    }
}


