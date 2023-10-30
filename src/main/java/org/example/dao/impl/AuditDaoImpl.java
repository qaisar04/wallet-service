package org.example.dao.impl;

import org.example.core.domain.Audit;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;
import org.example.dao.Dao;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AuditDao interface for interacting with the database and audit records.
 */

public class AuditDaoImpl implements Dao<Integer, Audit> {

    private static final AuditDaoImpl auditDaoImpl = new AuditDaoImpl();

    /**
     * Find an audit record by its identifier.
     *
     * @param id The identifier of the audit record.
     * @return An Optional with the found audit record or an empty Optional if the record is not found.
     */
    @Override
    public Optional<Audit> findById(Integer id) {
        String sqlFindById = """
                SELECT * FROM wallet.audits
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildAudit(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Find an audit record by the username.
     *
     * @param username The username for which to perform the search.
     * @return An Optional with the found audit record or an empty Optional if the record is not found.
     */
    public Optional<Audit> findByUsername(String username) {
        String sqlFindByUsername = """
                SELECT * FROM wallet.audits
                WHERE player_username=?;
                """;


        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByUsername)) {
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(buildAudit(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get a list of all audit records.
     *
     * @return A list of all audit records.
     */
    @Override
    public List<Audit> findAll() {
        String sqlFindAll = """
                SELECT * FROM wallet.audits;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Audit> audits = new ArrayList<>();

            while (resultSet.next()) {
                audits.add(buildAudit(resultSet));
            }

            return audits;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Save a new audit record in the database.
     *
     * @param audit The audit record to be saved.
     * @return The saved audit record with an assigned identifier.
     */
    @Override
    public Audit save(Audit audit) {
        String sqlSave = """
            INSERT INTO wallet.audits(player_username, audit_type, action_type)
            VALUES (?,?,?);
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, audit.getPlayerFullName());
            preparedStatement.setObject(2, audit.getAuditType(), Types.OTHER);
            preparedStatement.setObject(3, audit.getActionType(), Types.OTHER);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        audit.setId(keys.getObject(1, Integer.class));
                    }
                } catch (SQLException e) {
                    System.err.println("Ошибка при получении сгенерированного ключа: " + e.getMessage());
                }
            } else {
                System.err.println("Ошибка при выполнении SQL-запроса. Нет добавленных записей.");
            }

            return audit;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
        return null;
    }



    /**
     * Update an audit record in the database.
     *
     * @param audit The audit record to be updated.
     */
    @Override
    public void update(Audit audit) {
        String sqlUpdate = """
                UPDATE wallet.audits
                SET audit_type = ?,
                action_type = ?
                WHERE id = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setObject(1, audit.getAuditType());
            preparedStatement.setObject(2, audit.getActionType());
            preparedStatement.setObject(3, audit.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
    }

    /**
     * Delete an audit record by its identifier.
     *
     * @param id The identifier of the audit record to be deleted.
     * @return true if the record is successfully deleted; otherwise, false.
     */
    @Override
    public boolean delete(Integer id) {
        String sqlDeleteById = """
                DELETE FROM wallet.audits
                WHERE id = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete all audit records from the database.
     *
     * @return true if all records are successfully deleted; otherwise, false.
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteById = """
                DELETE FROM wallet.audits
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    private Audit buildAudit(ResultSet resultSet) throws SQLException {
        String auditTypeString = resultSet.getString("audit_type");
        AuditType auditType = AuditType.valueOf(auditTypeString);

        String actionTypeString = resultSet.getString("action_type");
        ActionType actionType = ActionType.valueOf(actionTypeString);

        return Audit.builder()
                .id(resultSet.getInt("id"))
                .auditType(auditType)
                .actionType(actionType)
                .playerFullName(resultSet.getString("player_username"))
                .build();
    }

    private AuditDaoImpl() {}

    public static AuditDaoImpl getInstance() {
        return auditDaoImpl;
    }
}



