package org.example.manager;

import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.exception.TransactionException;

import java.math.BigDecimal;
import java.util.*;

import static org.example.core.domain.types.ActionType.*;
import static org.example.core.domain.types.AuditType.*;
import static org.example.core.domain.types.TransactionType.*;
import static org.example.core.domain.types.TransactionType.CREDIT;


public class PlayerManager {

    private WalletPlayerService playerService = WalletPlayerService.getInstance();
    private WalletTransactionsService transactionsService = WalletTransactionsService.getInstance();
    private WalletAuditService auditService = WalletAuditService.getInstance();

    //TODO change admin add

    /**
     * The constructor creates a new Wallet Service object
     */
    public PlayerManager() { }

    /**
     * The method registers a new player with the specified name and password.
     *
     * @return true if registration is successful, otherwise false.
     */
    public Boolean registerPlayer(String username, String password) {
        Optional<Player> existingPlayer = playerService.findByUsername(username);

        if (existingPlayer.isEmpty()) {
            Player newPlayer = Player.builder()
                    .username(username)
                    .password(password)
                    .balance(BigDecimal.ZERO)
                    .build();
            playerService.save(newPlayer);
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
            audit(username, REGISTRATION, SUCCESS);
            return true;
        } else {
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку с другим именем пользователя!");
            audit(username, REGISTRATION, FAIL);
            return false;
        }
    }

    /**
     * The method authenticates the player by username and password.
     *
     * @return true if authentication is successful, otherwise false.
     */
    public boolean authenticatePlayer(String username, String password) {
        Player player = playerService.findByUsername(username).orElse(null);
        if (player != null && player.getPassword().equals(password)) {
            System.out.println("Вы успешно вошли в систему.");
            audit(username, ActionType.AUTHORIZATION, SUCCESS);
            return true;
        } else {
            // TODO: check this situation
            System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя и пароль.");
            audit(username, ActionType.AUTHORIZATION, FAIL);
            return false;
        }
    }

    /**
     * The method returns the balance of the player with the specified name.
     *
     * @param username The player's name.
     * @return The player's balance.
     */
    public BigDecimal getBalance(String username) {
        Player player = playerService.findByUsername(username).orElse(null);
        if (player != null) {
            audit(username, ActionType.BALANCE_INQUIRY, SUCCESS);
            return player.getBalance();
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            audit(username, ActionType.BALANCE_INQUIRY, FAIL);
            return BigDecimal.valueOf(-1.0);
        }
    }

    /**
     * The method performs a credit transaction for a player with a transaction ID.
     *
     * @param username     Player Name
     * @param customId Unique Transaction ID
     * @param amount        Transaction amount
     * @return true if the transaction is successful, otherwise false.
     */
    public boolean creditWithTransactionId(String username, Integer customId, BigDecimal amount) {

        if(customId > Integer.MAX_VALUE || customId <= 0) {
            System.out.println("Введите корректное число для идентификатороа транзакции.");
            audit(username, CREDIT_TRANSACTION, FAIL);
            return false;
        }

        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            System.out.println("Введите корректное число для выполнения кредитной транзакции.");
            audit(username, CREDIT_TRANSACTION, FAIL);
            return false;
        }

        Optional<Transaction> existingTransaction = transactionsService.findByCustomId(customId);

        if (existingTransaction.isPresent()) {
            System.out.println("Транзакция с таким идентификатором уже существует.");
            audit(username, CREDIT_TRANSACTION, FAIL);
            throw new TransactionException("Транзакция с таким идентификатором уже существует, пожалуйста, введите другой идентификатор");
        }

        Optional<Player> playerOptional = playerService.findByUsername(username);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            Transaction transaction = Transaction.createTransaction(customId, CREDIT, amount, player.getId());
            player.setBalance(player.getBalance().add(amount));

            transactionsService.save(transaction);
            playerService.update(player);

            System.out.println("Кредитная транзакция успешно выполнена.");
            audit(username, CREDIT_TRANSACTION, SUCCESS);
            return true;
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            audit(username, CREDIT_TRANSACTION, FAIL);
            return false;
        }
    }

    /**
     * The method performs a debit transaction for the player with the specified name.
     *
     * @param username     Player Name
     * @param сustomId Unique Transaction ID
     * @param amount        Transaction amount
     * @return true if the transaction is successful, otherwise false.
     */
    public boolean debitWithTransactionId(String username, Integer сustomId, BigDecimal amount) {

        if(сustomId > Integer.MAX_VALUE || сustomId <= 0) {
            System.out.println("Введите корректное число для идентификатороа транзакции.");
            audit(username, DEBIT_TRANSACTION, FAIL);
            return false;
        }

        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            System.out.println("Введите корректное число для выполнения дебетовой транзакции.");
            audit(username, DEBIT_TRANSACTION, FAIL);
            return false;
        }
        Optional<Player> playerOptional = null;

        try {
            playerOptional = playerService.findByUsername(username);
        } catch (Exception e) {
            audit(username, DEBIT_TRANSACTION, FAIL);
            System.err.println("ERROR: " + e);
        }

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            Optional<Transaction> existingTransaction = transactionsService.findById(сustomId);
            if (existingTransaction.isPresent()) {
                audit(username, DEBIT_TRANSACTION, FAIL);
                throw new TransactionException("Транзакция с таким идентификатором уже существует, пожалуйста, введите другой идентификатор");
            }

            if (player.getBalance().compareTo(amount) >= 0) {
                Transaction transaction =  Transaction.createTransaction(сustomId, DEBIT, amount, player.getId());
                player.setBalance(player.getBalance().subtract(amount));
                playerService.update(player);
                transactionsService.save(transaction);
                audit(username, DEBIT_TRANSACTION, SUCCESS);
                System.out.println("Дебетовая транзакция успешно выполнена.");
                return true;
            } else {
                System.out.println("Недостаточно средств для выполнения дебетовой транзакции.");
                audit(username, DEBIT_TRANSACTION, FAIL);
                return false;
            }
        } else {
            audit(username, DEBIT_TRANSACTION, FAIL);
            System.out.println("Пользователь с именем " + username + " не найден.");
            return false;
        }
    }

    /**
     * The method performs a credit transaction for a player without a transaction ID.
     *
     * @param username Player Name
     * @param amount   Transaction amount
     * @return true if the transaction is successful, otherwise false.
     */
    public boolean creditWithoutTransactionId(String username, BigDecimal amount) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);

        return creditWithTransactionId(username, randomInt, amount);
    }

    /**
     * The method performs a debit transaction for a player without a transaction ID.
     *
     * @param username Player Name
     * @param amount   Transaction amount
     * @return  true if the transaction is successful, otherwise false.
     */
    public boolean debitWithoutTransactionId(String username, BigDecimal amount) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);


        return debitWithTransactionId(username, randomInt, amount);
    }

    /**
     * Registers audit records of user actions.
     *
     * @param username   The name of the user for whom the audit is being registered.
     * @param actionType The type of action being performed (for example, REGISTRATION, AUTHORIZATION).
     * @param auditType  Type of audit result (for example, SUCCESS, FAIL).
     */
    public void audit(String username, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .playerFullName(username)
                .actionType(actionType)
                .auditType(auditType)
                .build();
        auditService.save(audit);
    }

}
