package kz.baltabayev.audits.service;

import kz.baltabayev.audits.domain.Audit;
import kz.baltabayev.audits.util.ConnectionManager;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;

@RequiredArgsConstructor
public class AuditService {

    private final ConnectionManager connectionManager;

    public Audit save(Audit audit) {
        String sqlSave = """
            INSERT INTO wallet.audits(player_username, audit_type, action_type)
            VALUES (?,?,?);
            """;

        try (Connection connection = connectionManager.getConnection();
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
}
