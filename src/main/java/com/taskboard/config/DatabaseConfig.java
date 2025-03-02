package com.taskboard.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final String DB_HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String URL = "jdbc:postgresql://" + DB_HOST + ":5432/mydatabase";

    private static final String USER = "myuser";
    private static final String PASSWORD = "mypassword";
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            var conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            throw new RuntimeException("Database connection error", e);
        }
    }
}