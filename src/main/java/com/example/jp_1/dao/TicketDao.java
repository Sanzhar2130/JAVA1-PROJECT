package com.example.jp_1.dao;

import com.example.jp_1.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    private final DatabaseConnection databaseConnection;
    public TicketDao(DatabaseConnection connection) {
        this.databaseConnection = connection;
    }

    private static final String INSERT_SQL = "INSERT INTO Ticket (bid, sess_id, sid, ttid, price) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Ticket SET bid=?, sess_id=?, sid=?, ttid=?, price=? WHERE tid=?";
    private static final String DELETE_SQL = "DELETE FROM Ticket WHERE tid=?";
    private static final String SELECT_ALL_SQL = "SELECT tid, bid, sess_id, sid, ttid, price FROM Ticket";
    private static final String SELECT_BY_ID_SQL = "SELECT tid, bid, sess_id, sid, ttid, price FROM Ticket WHERE tid=?";

    Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    public void save(Ticket ticket) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ticket.getBid());
            statement.setInt(2, ticket.getSessId());
            statement.setInt(3, ticket.getSid());
            statement.setInt(4, ticket.getTtId());
            statement.setDouble(5, ticket.getPrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ticket failed, no rows affected.");
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setTid(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void update(Ticket ticket) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, ticket.getBid());
            statement.setInt(2, ticket.getSessId());
            statement.setInt(3, ticket.getSid());
            statement.setInt(4, ticket.getTtId());
            statement.setDouble(5, ticket.getPrice());
            statement.setInt(6, ticket.getTid());
            statement.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Ticket> findAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_ALL_SQL);
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getInt("tid"),
                        rs.getInt("bid"),
                        rs.getInt("sess_id"),
                        rs.getInt("sid"),
                        rs.getInt("ttid"),
                        rs.getDouble("price")));
            }
        }
        return tickets;
    }

    public Ticket findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(
                            rs.getInt("tid"),
                            rs.getInt("bid"),
                            rs.getInt("sess_id"),
                            rs.getInt("sid"),
                            rs.getInt("ttid"),
                            rs.getDouble("price"));
                }
            }
        }
        return null;
    }

    public List<Integer> findOccupiedSid(int sessId) throws SQLException {
        String sql = "SELECT * FROM Ticket WHERE sess_id = ?";
        List<Integer> occupiedIds = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, sessId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    occupiedIds.add(rs.getInt("sid"));
                }
            }
        }
        return occupiedIds;
    }
}
