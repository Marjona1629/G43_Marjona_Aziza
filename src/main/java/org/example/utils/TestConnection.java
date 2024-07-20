package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {
    private static TestConnection instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private Connection connection;

    private TestConnection() {
        connect();
    }

    public static synchronized TestConnection getInstance() {
        if (instance == null) {
            instance = new TestConnection();
        }
        return instance;
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, "postgres", "root123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if the connection is closed", e);
        }
        return connection;
    }

    public synchronized Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create a statement", e);
        }
    }
}