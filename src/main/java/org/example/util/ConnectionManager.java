package org.example.util;


import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
@UtilityClass
public class ConnectionManager {

    /**
     * Get a database connection.
     *
     * @return A database connection.
     * @throws RuntimeException if a database connection cannot be established.
     */
    private static final String URL = "jdbc:postgresql://localhost:5432/wallet_service_db";
    private static final String USER_NAME = "qaisar";
    private static final String PASSWORD = "walletservice";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        }
    }
}