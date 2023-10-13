package org.example.service;


import lombok.Getter;
import org.example.types.ActionType;
import org.example.types.AuditType;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.types.TransactionType;

import java.math.BigDecimal;
import java.util.*;

/**
 * Класс WalletPlayerService представляет собой сервис для управления взаимодействием с игроками.
 * Он предоставляет следующие функциональности:
 * - Регистрация новых игроков.
 * - Аутентификация игроков.
 * - Проверка балансов игроков.
 */
public class WalletPlayerService {

    @Getter
    private Map<String, Player> players;
    @Getter
    private Map<String, Transaction> transactions;
    @Getter
    private List<String> audits;

    /**
     * Конструктор создает новый объект WalletService с пустыми списками игроков,
     * транзакций и аудита, и добавляет администратора по умолчанию.
     */
    public WalletPlayerService() {
        this.players = new HashMap<>();
        players.put("admin", new Player("admin", "admin"));
        this.transactions = new HashMap<>();
        this.audits = new ArrayList<>();
    }

    /**
     * Метод регистрирует нового игрока с указанным именем и паролем.
     *
     * @return true, если регистрация успешно выполнена, иначе false.
     */
    public boolean registerPlayer(String username, String password) {
        if (!players.containsKey(username)) {
            Player newPlayer = new Player(username, password);
            players.put(username, newPlayer);
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
            audit(username, ActionType.РЕГИСТРАЦИЯ, AuditType.SUCCESS);
            return true;
        } else {
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку, вызовите заново команду регистрации!");
            audit(username, ActionType.РЕГИСТРАЦИЯ, AuditType.FAIL);
            return false;
        }
    }

    /**
     * Метод аутентифицирует игрока по имени пользователя и паролю.
     *
     * @return true, если аутентификация успешно выполнена, иначе false.
     */
    public boolean authenticatePlayer(String username, String password) {
        Player player = players.get(username);
        if (player != null && player.getPassword().equals(password)) {
            System.out.println("Вы успешно вошли в систему.");
            audit(username, ActionType.АВТОРИЗАЦИЯ, AuditType.SUCCESS);
            return true;
        } else {
            System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя и пароль.");
            audit(username, ActionType.АВТОРИЗАЦИЯ, AuditType.FAIL);
            return false;
        }
    }

    /**
     * Метод возвращает баланс игрока с указанным именем.
     *
     * @param username Имя игрока.
     * @return Баланс игрока.
     */
    public BigDecimal getBalance(String username) {
        Player player = players.get(username);
        if (player != null) {
            audit(username, ActionType.ПОЛУЧЕНИЕ_БАЛАНСА, AuditType.SUCCESS);
            return player.getBalance();
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            audit(username, ActionType.ПОЛУЧЕНИЕ_БАЛАНСА, AuditType.FAIL);
            return BigDecimal.valueOf(-1.0);
        }
    }

    /**
     * Метод выполняет кредитную транзакцию для игрока с идентификатором транзакции.
     *
     * @param username      Имя игрока
     * @param transactionId Уникальный идентификатор транзакции
     * @param amount        Сумма транзакции
     * @return true, если транзакция успешно выполнена, иначе false.
     */
    public boolean creditWithTransactionId(String username, String transactionId, BigDecimal amount) {
        if (transactions.containsKey(transactionId) && transactionId != null) {
            System.out.println("Транзакция с таким идентификатором уже существует.");
            audit(username, ActionType.КРЕДИТНАЯ_ТРАНЗАКЦИЯ, AuditType.FAIL);
            return false;
        } else {
            Transaction transaction = new Transaction(transactionId, TransactionType.CREDIT, amount);
            Player player = players.get(username);

            if (player != null) {
                player.getTransactionHistory().add(transaction);
                player.setBalance(player.getBalance().add(amount));
                transactions.put(transactionId, transaction);
                System.out.println("Кредитная транзакция успешно выполнена.");
                audit(username, ActionType.КРЕДИТНАЯ_ТРАНЗАКЦИЯ, AuditType.SUCCESS);
                return true;
            } else {
                System.out.println("Пользователь с именем " + username + " не найден.");
                audit(username, ActionType.КРЕДИТНАЯ_ТРАНЗАКЦИЯ, AuditType.FAIL);
                return false;
            }
        }
    }


    /**
     * Метод выполняет дебетовую транзакцию для игрока с указанным именем.
     *
     * @param username      Имя игрока.
     * @param transactionId Уникальный идентификатор транзакции
     * @param amount        Сумма транзакции
     * @return true, если транзакция успешно выполнена, иначе false.
     */
    public boolean debitWithTransactionId(String username, String transactionId, BigDecimal amount) {
        Player player = players.get(username);
        if (player != null) {
            if (player.getBalance().compareTo(amount) >= 0) {
                if (transactions.containsKey(transactionId)) {
                    System.out.println("Транзакция с таким идентификатором уже существует.");
                    audit(username, ActionType.ДЕБЕТОВАЯ_ТРАНЗАКЦИЯ, AuditType.FAIL);
                    return false;
                }
                Transaction transaction = new Transaction(transactionId, TransactionType.DEBIT, amount);
                player.getTransactionHistory().add(transaction);
                player.setBalance(player.getBalance().subtract(amount));
                transactions.put(transactionId, transaction);
                audit(username, ActionType.ДЕБЕТОВАЯ_ТРАНЗАКЦИЯ, AuditType.SUCCESS);
                System.out.println("Дебетовая транзакция успешно выполнена.");
                return true;
            } else {
                System.out.println("Недостаточно средств для выполнения дебетовой транзакции.");
                audit(username, ActionType.ДЕБЕТОВАЯ_ТРАНЗАКЦИЯ, AuditType.FAIL);
                return false;
            }
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            audit(username, ActionType.ДЕБЕТОВАЯ_ТРАНЗАКЦИЯ, AuditType.FAIL);
            return false;
        }
    }

    /**
     * Метод выполняет кредитную транзакцию для игрока без идентификатора транзакции.
     *
     * @param username Имя игрока
     * @param amount   Сумма транзакции
     * @return true, если транзакция успешно выполнена, иначе false.
     */
    public boolean creditWithoutTransactionId(String username, BigDecimal amount) {
        UUID transactionId = UUID.randomUUID();
        String transactionIdString = transactionId.toString();

        return creditWithTransactionId(username, transactionIdString, amount);
    }

    /**
     * Метод выполняет дебетовую транзакцию для игрока без идентификатора транзакции.
     *
     * @param username Имя игрока
     * @param amount   Сумма транзакции
     * @return true, если транзакция успешно выполнена, иначе false.
     */
    public boolean debitWithoutTransactionId(String username, BigDecimal amount) {
        UUID transactionId = UUID.randomUUID();
        String transactionIdString = transactionId.toString();

        return debitWithTransactionId(username, transactionIdString, amount);
    }

    /**
     * Регистрирует аудиторские записи о действиях пользователя.
     *
     * @param username   Имя пользователя, для которого регистрируется аудит.
     * @param actionType Тип выполняемого действия (например, РЕГИСТРАЦИЯ, АВТОРИЗАЦИЯ).
     * @param auditType  Тип результата аудита (например, SUCCESS - успешно, FAIL - неудачно).
     */
    public void audit(String username, ActionType actionType, AuditType auditType) {
        String s = "Пользователь " + username + ": " + actionType + " | " + auditType;
        audits.add(s);
    }
}
