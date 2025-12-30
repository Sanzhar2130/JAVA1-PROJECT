package com.example.jp_1.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<T, K> {
    protected DatabaseConnection databaseConnection;
    public GenericDao(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    public abstract void save(T entity) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void delete(K id) throws SQLException;
    public abstract T findById(K id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;


}
