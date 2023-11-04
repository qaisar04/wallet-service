package org.example.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.util.ConnectionManager;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The `LiquibaseDemo` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * changesets defined in a changelog file to the connected database.
 */
@Component
public class LiquibaseDemo {

    private final ConnectionManager connectionManager;

    public LiquibaseDemo(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @PostConstruct
    public void init() {
        runMigrations();
    }

    public static void main(String[] args) {
        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(new ConnectionManager());
        liquibaseDemo.runMigrations();
    }
    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA);
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("migration");

            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
