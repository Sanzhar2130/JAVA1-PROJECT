package com.example.jp_1.dao;

import com.example.jp_1.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MovieDao extends GenericDao<Movie, Integer> {
    public MovieDao(DatabaseConnection connection) {
        super(connection);
    }

    private static final String INSERT_SQL = "INSERT INTO Movie (gid, title, director, duration_minutes, description) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE Movie SET gid=?, title=?, director=?, duration_minutes=?, description=? WHERE mid=?";
    private static final String DELETE_SQL = "DELETE FROM Movie WHERE mid=?";
    private static final String SELECT_ALL_SQL = "SELECT mid, gid, title, director, duration_minutes, description FROM Movie";
    private static final String SELECT_BY_ID_SQL = "SELECT mid, gid, title, director, duration_minutes, description FROM Movie WHERE mid=?";

    @Override
    public void save(Movie movie) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, movie.getGid());
            statement.setString(2, movie.getTitle());
            statement.setString(3, movie.getDirector());
            statement.setInt(4, movie.getDurationMinutes());
            statement.setString(5, movie.getDescription());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movie.setMid(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating movie failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Movie movie) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, movie.getGid());
            statement.setString(2, movie.getTitle());
            statement.setString(3, movie.getDirector());
            statement.setInt(4, movie.getDurationMinutes());
            statement.setString(5, movie.getDescription());
            statement.setInt(6, movie.getMid());
            statement.executeUpdate();

            if (movie.getGid() != null) {
                statement.setInt(5, movie.getGid());
            } else {
                statement.setNull(5, java.sql.Types.INTEGER);
            }
            statement.setInt(6, movie.getMid());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer mid) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, mid);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Movie> findAll() throws SQLException{
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
             ResultSet rs = statement.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
        }
        return movies;
    }

    @Override
    public Movie findById(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovie(rs);
                }
            }
        }
        return null;
    }

    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getInt("mid"),
                rs.getInt("gid"),
                rs.getString("title"),
                rs.getString("director"),
                rs.getInt("duration_minutes"),
                rs.getString("description")
        );
    }
}


