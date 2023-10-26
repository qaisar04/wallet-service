package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
public class ConnectionManager {

    /**
     * Get a database connection.
     *
     * @return A database connection.
     * @throws RuntimeException if a database connection cannot be established.
     */
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    public static final String DRIVER_KEY = "db.driver";

    public static Connection getConnection() {
        try {

            Class.forName(PropertiesUtil.get(DRIVER_KEY));

            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}