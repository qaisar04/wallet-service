package org.example.service;

import org.example.entity.Transaction;
import org.example.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс WalletService представляет собой сервис для управления кошельками игроков.
 * Он позволяет регистрировать игроков, аутентифицировать их, проверять балансы,
 * выполнять дебетовые и кредитные транзакции, а также просматривать историю транзакций.
 */
public class WalletService {

    private Map<String, Player> players;
    private Map<String, Transaction> transactions;
    private List<String> audits;

    /**
     * Конструктор создает новый объект WalletService с пустыми списками игроков,
     * транзакций и аудита, и добавляет администратора по умолчанию.
     */
    public WalletService() {
        this.players = new HashMap<>();
        players.put("admin", new Player("admin", "admin"));
        this.transactions = new HashMap<>();
        this.audits = new ArrayList<>();
    }

    /**
     * Метод регистрирует нового игрока с указанным именем и паролем.
     * @param username Имя игрока.
     * @param password Пароль игрока.
     * @return true, если регистрация успешно выполнена, иначе false.
     */
    public boolean registerPlayer(String username, String password) {
        if (!players.containsKey(username)) {
            Player newPlayer = new Player(username, password);
            players.put(username, newPlayer);
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
            audit(username, "Регистрация");
            return true;
        } else {
            System.out.println("Игрок с именем " + username + " уже существует. Повторите попытку, вызовите заново команду регистрации!");
            return false;
        }
    }

    /**
     * Метод аутентифицирует игрока по имени пользователя и паролю.
     * @param username Имя игрока.
     * @param password Пароль игрока.
     * @return true, если аутентификация успешно выполнена, иначе false.
     */
    public boolean authenticatePlayer(String username, String password) {
        Player player = players.get(username);
        if (player != null && player.getPassword().equals(password)) {
            System.out.println("Вы успешно вошли в систему.");
            audit(username, "Авторизация");
            return true;
        } else {
            System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя и пароль.");
            return false;
        }
    }

    /**
     * Метод возвращает баланс игрока с указанным именем.
     * @param username Имя игрока.
     * @return Баланс игрока.
     */
    public double getBalance(String username) {
        Player player = players.get(username);
        if (player != null) {
            audit(username, "Получение баланса");
            return player.getBalance();
        } else {
            System.out.println("Игрок с именем " + username + " не найден.");
            return -1.0;
        }
    }

    /**
     * Метод выполняет дебетовую транзакцию для игрока с указанным именем.
     * @param username Имя игрока.
     * @param transactionId Идентификатор транзакции.
     * @param amount Сумма дебетовой транзакции.
     * @return true, если транзакция успешно выполнена, иначе false.
     * @throws Exception Если возникает ошибка при выполнении транзакции.
     */
    public boolean debit(String username, String transactionId, double amount) throws Exception {
        Player player = players.get(username);
        if (player != null) {
            if (player.getBalance() >= amount) {

                // Проверка уникальности идентификатора транзакции
                if (transactions.containsKey(transactionId)) {
                    System.out.println("Транзакция с таким идентификатором уже существует.");
                    return false;
                }

                Transaction transaction = new Transaction(transactionId, "debit", amount);
                player.getTransactionHistory().add(transaction);
                player.setBalance(player.getBalance() - amount);
                transactions.put(transactionId, transaction);
                audit(username, "Дебетовая транзакция");
                System.out.println("Дебетовая транзакция успешно выполнена.");
                return true;
            } else {
                System.out.println("Недостаточно средств для выполнения дебетовой транзакции.");
                return false;
            }
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            return false;
        }
    }

    /**
     * Метод выполняет кредитную транзакцию для игрока с указанным именем.
     * @param username Имя игрока.
     * @param transactionId Идентификатор транзакции.
     * @param amount Сумма кредитной транзакции.
     * @return true, если транзакция успешно выполнена, иначе false.
     */
    public boolean credit(String username, String transactionId, double amount) {
        Player player = players.get(username);
        if (player != null) {
            if (transactions.containsKey(transactionId)) {
                System.out.println("Транзакция с таким идентификатором уже существует.");
                return false;
            } else {
                Transaction transaction = new Transaction(transactionId, "credit", amount);
                player.getTransactionHistory().add(transaction);
                player.setBalance(player.getBalance() + amount);
                transactions.put(transactionId, transaction);
                audit(username, "Кредитная транзакция");
                System.out.println("Кредитная транзакция успешно выполнена.");
                return true;
            }
        } else {
            System.out.println("Игрок с именем " + username + " не найден.");
            return false;
        }
    }

    /**
     * Метод просматривает историю транзакций для игрока с указанным именем.
     * @param username Имя игрока.
     * @return Количество транзакций в истории.
     */
    public int viewTransactionHistory(String username) {
        Player player = players.get(username);
        if (player != null) {
            List<Transaction> history = player.getTransactionHistory();
            if (history != null) { // Проверка на null перед вызовом isEmpty()
                if (history.isEmpty()) {
                    System.out.println("У игрока " + username + " нет истории транзакций.");
                    return 0;
                } else {
                    System.out.println("История транзакций для пользователя " + username + ":");
                    for (Transaction transaction : history) {
                        System.out.println("Идентификатор транзакции: " + transaction.getId() + " | " + transaction.getType() + " " + transaction.getAmount());
                    }
                    audit(username, "Просмотр истории транзакций");
                    return history.size();
                }
            } else {
                System.out.println("История транзакций игрока " + username + " не инициализирована.");
                return -1;
            }
        } else {
            System.out.println("Игрок с именем " + username + " не найден.");
            return 0;
        }
    }

    /**
     * Метод просматривает аудит действий игроков.
     * @return Количество записей в аудите.
     */
    public int viewAllAudits() {
        for (String audit : audits) {
            System.out.println(audit);
        }
        return audits.size();
    }

    /**
     * Метод добавляет запись в аудит действий игроков.
     * @param username Имя игрока.
     * @param action Действие, которое было выполнено.
     */
    public void audit(String username, String action) {
        String s = "Игрок " + username + ": " + action;
        audits.add(s);
    }
}
