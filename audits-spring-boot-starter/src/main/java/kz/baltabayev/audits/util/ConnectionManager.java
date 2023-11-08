package kz.baltabayev.audits.util;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
@Component
public class ConnectionManager {

    /**
     * Get a database connection.
     *
     * @return A database connection.
     * @throws RuntimeException if a database connection cannot be established.
     */
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection() {
        try {
            Class.forName(driver);

            return DriverManager.getConnection(
                    url,
                    username,
                    password
            );
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}