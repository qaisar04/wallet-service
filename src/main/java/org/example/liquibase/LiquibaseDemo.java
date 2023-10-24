package org.example.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.util.ConnectionManager;

import java.sql.Connection;

/**
 * The `LiquibaseDemo` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * changesets defined in a changelog file to the connected database.
 */
public class LiquibaseDemo {

    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static LiquibaseDemo liquibaseDemo = new LiquibaseDemo();

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try {
            Connection connection = ConnectionManager.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the `LiquibaseDemo` class.
     *
     * @return The singleton instance.
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}
