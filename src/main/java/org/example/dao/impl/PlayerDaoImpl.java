package org.example.dao.impl;

import lombok.SneakyThrows;
import org.example.dao.PlayerDao;
import org.example.core.domain.Player;
import org.example.exception.PlayerNotFoundException;
import org.example.manager.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerDaoImpl implements PlayerDao<Integer, Player> {

    private static final PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl();

    @Override
    public Optional<Player> findById(Integer id) {
        String sqlFindById = """
                SELECT * FROM wallet_service_db.players
                WHERE id=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
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
                SELECT * FROM wallet_service_db.players
                WHERE full_name=?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
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
                SELECT * FROM wallet_service_db.players;
                """;

        try (Connection connection = ConnectionManager.getConnection();
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
                INSERT INTO wallet_service_db.players(full_name, password, balance)
                VALUES (?,?,?);
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, player.getFullName());
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
                UPDATE wallet_service_db.players
                SET full_name = ?,
                balance = ?
                WHERE id = ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {

            preparedStatement.setString(1, player.getFullName());
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
                DELETE FROM wallet_service_db.players
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

    @Override
    public boolean deleteAll() {
        String sqlDeleteById = """
                DELETE FROM wallet_service_db.players
                """;

        try (Connection connection = ConnectionManager.getConnection();
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
                .fullName(resultSet.getString("full_name"))
                .password(resultSet.getString("password"))
                .balance(resultSet.getBigDecimal("balance"))
                .build();
    }

    public static PlayerDaoImpl getInstance() {
        return playerDaoImpl;
    }
}
