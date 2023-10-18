package org.example.dao.impl;

import org.example.core.domain.types.TransactionType;
import org.example.dao.TransactionDao;
import org.example.core.domain.Transaction;
import org.example.manager.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the TransactionDao interface for managing transaction data in the "wallet_service_db" database.
 */
public class TransactionDaoImpl implements TransactionDao<Integer, Transaction> {

    private static final TransactionDaoImpl transactionDaoImpl = new TransactionDaoImpl();

    /**
     * Retrieve a transaction by its ID.
     *
     * @param id The ID of the transaction to retrieve.
     * @return An Optional containing the retrieved transaction, or an empty Optional if not found.
     */
    @Override
    public Optional<Transaction> findById(Integer id) {
        String sqlFindById = """
                SELECT * FROM wallet_service_db.transactions
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildTransaction(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieve a transaction by its custom ID.
     *
     * @param customId The custom ID of the transaction to retrieve.
     * @return An Optional containing the retrieved transaction, or an empty Optional if not found.
     */
    public Optional<Transaction> findByCustomId(Integer customId) {
        String sqlFindByCustomId = """
                SELECT * FROM wallet_service_db.transactions
                WHERE custom_id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByCustomId)) {
            preparedStatement.setObject(1, customId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildTransaction(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieve all transactions associated with a specific player.
     *
     * @param playerId The ID of the player for whom transactions are to be retrieved.
     * @return A list of transactions associated with the specified player.
     */
    public List<Transaction> findByPlayerId(Integer playerId) {
        String sqlFindById = """
                SELECT * FROM wallet_service_db.transactions
                WHERE player_id=?;
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = buildTransaction(resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Retrieve all transactions from the database.
     *
     * @return A list of all transactions stored in the database.
     */
    @Override
    public List<Transaction> findAll() {
        String sqlFindAll = """
                SELECT * FROM wallet_service_db.transactions;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(buildTransaction(resultSet));
            }

            return transactions;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Save a new transaction or update an existing one in the database.
     *
     * @param transaction The transaction to save or update.
     * @return The saved or updated transaction.
     */
    @Override
    public Transaction save(Transaction transaction) {
        String sqlSave = """
                INSERT INTO wallet_service_db.transactions(custom_id, type, amount, player_id)
                VALUES (?,?,?,?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, transaction.getCustomId());
            preparedStatement.setObject(2, transaction.getType(), Types.OTHER);
            preparedStatement.setObject(3, transaction.getAmount());
            preparedStatement.setObject(4, transaction.getPlayerId());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                transaction.setId(keys.getInt("id"));
            }


            return transaction;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return transaction;
        }
    }

    /**
     * Update an existing transaction in the database.
     *
     * @param transaction The transaction to update.
     */
    @Override
    public void update(Transaction transaction) {
        String sqlUpdate = """
                UPDATE wallet_service_db.transactions
                SET type = ?,
                    amount = ?
                WHERE id = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setObject(1, transaction.getType(), Types.OTHER);
            preparedStatement.setObject(2, transaction.getAmount());
            preparedStatement.setObject(3, transaction.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
    }

    /**
     * Delete a transaction by its ID.
     *
     * @param id The ID of the transaction to delete.
     * @return True if the transaction is successfully deleted, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        String sqlDeleteById = """
                DELETE FROM wallet_service_db.transactions
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
     * Delete all transactions from the database.
     *
     * @return True if all transactions are successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteAll() {
        String sqlDeleteById = """
                DELETE FROM wallet_service_db.transactions
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    /**
     * Build a Transaction object from a ResultSet.
     *
     * @param resultSet The ResultSet containing transaction data.
     * @return A Transaction object built from the ResultSet data.
     * @throws SQLException if a SQL-related error occurs.
     */
    private Transaction buildTransaction(ResultSet resultSet) throws SQLException {
        String transactionTypeString = resultSet.getString("type");
        TransactionType transactionType = TransactionType.valueOf(transactionTypeString);

        return Transaction.builder()
                .customId(resultSet.getInt("custom_id"))
                .type(transactionType)
                .amount(resultSet.getBigDecimal("amount"))
                .playerId(resultSet.getInt("player_id"))
                .build();
    }

    /**
     * Get the singleton instance of the TransactionDaoImpl class.
     *
     * @return The singleton instance of TransactionDaoImpl.
     */
    public static TransactionDaoImpl getInstance() {
        return transactionDaoImpl;
    }


}
