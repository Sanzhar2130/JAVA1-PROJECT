package com.example.jp_1.dao;

import com.example.jp_1.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDao extends GenericDao<Session, Integer> {
    public SessionDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO Session (mid, hid, start_time, base_price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Session SET mid=?, hid=?, start_time=?, base_price=? WHERE sess_id=?";
    private static final String DELETE_SQL = "DELETE FROM Session WHERE sess_id=?";
    private static final String SELECT_ALL_SQL = "SELECT sess_id, mid, hid, start_time, base_price FROM Session";
    private static final String SELECT_BY_ID_SQL = "SELECT sess_id, mid, hid, start_time, base_price FROM Session WHERE sess_id=?";

    @Override
    public void save(Session session) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, session.getMid());
            statement.setInt(2, session.getHid());
            statement.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            statement.setDouble(4, session.getBasePrice());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating session failed, no rows affected.");
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setSessId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Session session) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, session.getMid());
            statement.setInt(2, session.getHid());
            statement.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            statement.setDouble(4, session.getBasePrice());
            statement.setInt(5, session.getSessId());
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
    public List<Session> findAll() throws SQLException {
        List<Session> sessions = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("sess_id"),
                        rs.getInt("mid"),
                        rs.getInt("hid"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getDouble("base_price")));
            }
        }
        return sessions;
    }

    @Override
    public Session findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Session(
                            rs.getInt("sess_id"),
                            rs.getInt("mid"),
                            rs.getInt("hid"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getDouble("base_price"));
                }
            }
        }
        return null;
    }
}
