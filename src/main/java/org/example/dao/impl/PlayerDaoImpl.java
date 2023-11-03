package org.example.dao.impl;

import org.example.dao.Dao;
import org.example.core.domain.Player;
import org.example.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerDaoImpl implements Dao<Integer, Player> {

    private ConnectionManager connectionManager;

    @Autowired
    public PlayerDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Player> findById(Integer id) {
        String sqlFindById = """
                SELECT * FROM wallet.players
                WHERE id=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildUser(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Player> findByUsername(String username) {
        String sqlFindByUsername = """
                SELECT * FROM wallet.players
                WHERE username=?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByUsername)) {
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(buildUser(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public List<Player> findAll() {
        String sqlFindAll = """
                SELECT * FROM wallet.players;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAll)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Player> players = new ArrayList<>();

            while (resultSet.next()) {
                players.add(buildUser(resultSet));
            }

            return players;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Player save(Player player) {
        String sqlSave = """
                INSERT INTO wallet.players(username, password, balance)
                VALUES (?,?,?);
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            preparedStatement.setBigDecimal(3, player.getBalance());

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                player.setId(keys.getObject("id", Integer.class));
            }

            return player;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Player player) {
        String sqlUpdate = """
                UPDATE wallet.players
                SET username = ?,
                balance = ?
                WHERE id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setBigDecimal(2, player.getBalance());
            preparedStatement.setObject(3, player.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sqlDeleteById = """
                DELETE FROM wallet.players
                WHERE id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        String sqlDeleteById = """
                DELETE FROM wallet.players
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteById)) {

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
            return false;
        }
    }

    private Player buildUser(ResultSet resultSet) throws SQLException {

        return Player.builder()
                .id(resultSet.getObject("id", Integer.class))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .balance(resultSet.getBigDecimal("balance"))
                .build();
    }

    // TODO: изменить получение обьекта в тестах
    private static final PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();

    public static PlayerDaoImpl getInstance() {
        return playerDaoImpl;
    }

    private PlayerDaoImpl() {
    }
}
