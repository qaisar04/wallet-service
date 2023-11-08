package org.example.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.util.ConnectionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class LiquibaseRun {

    private final ConnectionManager connectionManager;

    @Autowired
    public LiquibaseRun(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @PostConstruct
    public void init() {
        runMigrations();
    }

    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

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
