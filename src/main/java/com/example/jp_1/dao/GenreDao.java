package com.example.jp_1.dao;

import com.example.jp_1.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDao extends GenericDao<Genre, Integer>{
    public GenreDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO Genre (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE Genre SET name=? WHERE gid=?";
    private static final String DELETE_SQL = "DELETE FROM Genre WHERE gid=?";
    private static final String SELECT_ALL_SQL = "SELECT gid, name FROM Genre";
    private static final String SELECT_BY_ID_SQL = "SELECT gid, name FROM Genre WHERE gid=?";

    @Override
    public void save(Genre genre) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(INSERT_SQL)) {
            stmt.setString(1, genre.getName());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setGid(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Genre genre) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, genre.getName());
            statement.setInt(2, genre.getGid());
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
    public List<Genre> findAll() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("gid"), rs.getString("name")));
            }
        }
        return genres;
    }

    @Override
    public Genre findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    return new Genre(rs.getInt("gid"), rs.getString("name"));
                }
            }
        }
        return null;
    }

}
