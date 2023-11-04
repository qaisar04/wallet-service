package org.example.manager;

import io.jsonwebtoken.Claims;
import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.util.JwtUtils;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static org.example.core.domain.types.ActionType.*;
import static org.example.core.domain.types.AuditType.*;
import static org.example.core.domain.types.TransactionType.*;
import static org.example.core.domain.types.TransactionType.CREDIT;

/**
 * The {@code PlayerManager} class is a component in the Spring application responsible for managing player-related
 * operations. It handles player registration, authentication, balance inquiries, and credit/debit transactions.
 *
 * <p>PlayerManager interacts with services responsible for player management, transactions, and auditing. It uses JWT
 * tokens for authentication and authorization.
 *
 * <p>This class provides the following functionalities:
 * - Player registration: It allows the registration of new players by creating player accounts.
 * - Player authentication: It verifies the player's credentials and generates JWT tokens for authenticated players.
 * - Balance inquiry: It retrieves the balance of a player.
 * - Credit and debit transactions: It performs credit and debit transactions for players.
 *
 * <p>Example usage:
 * <pre>
 * // Create an instance of PlayerManager in your Spring application.
 * &#64;Autowired
 * private PlayerManager playerManager;
 * </pre>
 *
 * The class is annotated with `@Component` for Spring component scanning.
 */
@Component
public class PlayerManager {

    private WalletPlayerService playerService;
    private WalletTransactionsService transactionsService;
    private WalletAuditService auditService;
    private JwtUtils jwtUtils;


    @Autowired
    public PlayerManager(WalletPlayerService playerService, WalletTransactionsService transactionsService, WalletAuditService auditService, JwtUtils jwtUtils) {
        this.playerService = playerService;
        this.transactionsService = transactionsService;
        this.auditService = auditService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * The constructor creates a new Wallet Service object
     */
    public PlayerManager() {
    }

    /**
     * Registers a new player with the specified username and password.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player registration details.
     * @return A ResponseEntity containing the registered player.
     */
    public ResponseEntity<Player> registerPlayer(PlayerWrapper playerWrapper) {
        Player player = new Player(playerWrapper.getUsername(), playerWrapper.getPassword());
        Optional<Player> existingPlayer = playerService.findByUsername(player.getUsername());

        if (existingPlayer.isEmpty()) {
            Player newPlayer = Player.builder()
                    .username(player.getUsername())
                    .password(player.getPassword())
                    .balance(BigDecimal.ZERO)
                    .build();
            playerService.save(newPlayer);
            audit(player.getUsername(), REGISTRATION, SUCCESS);
            return ResponseEntity.ok(newPlayer);
        } else {
            audit(player.getUsername(), REGISTRATION, FAIL);
            return ResponseEntity.badRequest().body(new Player("error", "error"));
        }
    }

    /**
     * Authenticates the player by username and password, generating a JWT token upon successful authentication.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player authentication details.
     * @return A ResponseEntity containing a JWT token upon successful authentication.
     */
    public ResponseEntity<Map<String, String>> authenticatePlayer(PlayerWrapper playerWrapper) {
        Player player = new Player(playerWrapper.getUsername(), playerWrapper.getPassword());

        Optional<Player> optionalPlayer = Optional.ofNullable(playerService.findByUsername(player.getUsername()).orElse(null));
        if (optionalPlayer.isPresent() && optionalPlayer.get().getPassword().equals(player.getPassword())) {

            audit(playerWrapper.getUsername(), AUTHORIZATION, SUCCESS);
            String jwt = jwtUtils.generateToken(optionalPlayer.get());

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Authentication is successful");
            responseMap.put("jwt", jwt);
            return ResponseEntity.ok(responseMap);
        } else {
            Map<String, String> errorMap = Map.of("error", "Authentication is failed");
            audit(playerWrapper.getUsername(), AUTHORIZATION, FAIL);
            return ResponseEntity.badRequest().body(errorMap);
        }
    }

    /**
     * Retrieves the balance of the player with the specified username.
     *
     * @param token      The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> getBalance(String token) {
        Map<String, String> response = new HashMap<>();

        if (!token.startsWith("Bearer ")) {
            response.put("error", "Несанкционированный доступ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getPayload(token);
            String username = (String) claims.get("username");

            Player player = playerService.findByUsername(username).orElse(null);
            if (player != null) {
                audit(username, BALANCE_INQUIRY, SUCCESS);
                String balance = String.valueOf(player.getBalance());
                response.put("balance", balance);
                return ResponseEntity.ok(response);
            } else {
                audit(username, BALANCE_INQUIRY, FAIL);
                response.put("error", "Игрок не найден в базе данных");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        response.put("error", "Неавторизованный пользователь");
        return ResponseEntity.badRequest().body(response);
    }


    /**
     * Performs a credit transaction for a player using a transaction ID, checking for authorization and validation.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details.
     * @param token      The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> creditWithTransactionId(TransactionWithId transaction, String token) {
        Map<String, String> response = new HashMap<>();

        if (!token.startsWith("Bearer ")) {
            response.put("error", "Несанкционированный доступ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getPayload(token);
            String username = (String) claims.get("username");

            if (transaction.id() <= 0 || transaction.amount().compareTo(BigDecimal.ZERO) <= 0) {
                audit(username, CREDIT_TRANSACTION, FAIL);
                response.put("error", "Неверные детали транзакции");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<Player> optionalPlayer = playerService.findByUsername(username);

            if (optionalPlayer.isEmpty()) {
                response.put("error", "Игрок не найден");
                audit(username, CREDIT_TRANSACTION, FAIL);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Player player = optionalPlayer.get();

            if (optionalPlayer.get().getUsername().equals(username)) {
                Transaction successTransaction = Transaction.createTransaction(transaction.id(), CREDIT, transaction.amount(), player.getId());
                player.setBalance(player.getBalance().add(transaction.amount()));

                Optional<Transaction> existingTransaction = transactionsService.findById(transaction.id());

                if (existingTransaction.isPresent()) {
                    audit(username, CREDIT_TRANSACTION, FAIL);
                    response.put("error", "Транзакция уже существует");
                    return ResponseEntity.badRequest().body(response);
                }

                transactionsService.save(successTransaction);
                playerService.update(player);
                audit(username, CREDIT_TRANSACTION, SUCCESS);
                response.put("message", "Транзакция успешно завершена");
                return ResponseEntity.ok(response);

            } else {
                audit(username, CREDIT_TRANSACTION, FAIL);
                response.put("error", "Сбой кредитной транзакции");
                return ResponseEntity.badRequest().body(response);
            }
        }
        response.put("error", "Неавторизованный пользователь");
        return ResponseEntity.badRequest().body(response);
    }


    /**
     * Performs a debit transaction for a player using a transaction ID, checking for authorization and validation.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details.
     * @param token      The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> debitWithTransactionId(TransactionWithId transaction, String token) {
        Map<String, String> response = new HashMap<>();

        if (!token.startsWith("Bearer ")) {
            response.put("error", "Несанкционированный доступ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getPayload(token);
            String username = (String) claims.get("username");

            if (transaction.id() <= 0 || transaction.amount().compareTo(BigDecimal.ZERO) <= 0) {
                audit(username, DEBIT_TRANSACTION, FAIL);
                response.put("error", "Неверные детали транзакции");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<Player> optionalPlayer = playerService.findByUsername(username);

            if (optionalPlayer.isEmpty()) {
                audit(username, DEBIT_TRANSACTION, FAIL);
                response.put("error", "Игрок не найден");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Player player = optionalPlayer.get();

            if (optionalPlayer.get().getUsername().equals(username)) {
                Transaction successTransaction = Transaction.createTransaction(transaction.id(), DEBIT, transaction.amount(), player.getId());
                if(player.getBalance().compareTo(transaction.amount()) >= 0) {
                    Optional<Transaction> existingTransaction = transactionsService.findById(transaction.id());

                    if (existingTransaction.isPresent()) {
                        audit(username, DEBIT_TRANSACTION, FAIL);
                        response.put("error", "Транзакция уже существует");
                        return ResponseEntity.badRequest().body(response);
                    }

                    player.setBalance(player.getBalance().subtract(transaction.amount()));
                    transactionsService.save(successTransaction);
                    playerService.update(player);
                    audit(username, DEBIT_TRANSACTION, SUCCESS);
                    response.put("message", "Транзакция успешно завершена");
                    return ResponseEntity.ok(response);
                } else {
                    audit(username, DEBIT_TRANSACTION, FAIL);
                    response.put("error", "Сбой дебетной транзакции, недостаточно средств для выполнения транзакции.");
                    return ResponseEntity.badRequest().body(response);
                }

            } else {
                audit(username, DEBIT_TRANSACTION, FAIL);
                response.put("error", "Сбой дебетной транзакции");
                return ResponseEntity.badRequest().body(response);
            }
        }
        response.put("error", "Неавторизованный пользователь");
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Performs a credit transaction for a player without a specific transaction ID, generating a random ID.
     *
     * @param transactionWithoutId A {@code TransactionWithoutId} object containing transaction details.
     * @param token                The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> creditWithoutTransactionId(TransactionWithoutId transactionWithoutId, String token) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);
        TransactionWithId transaction = new TransactionWithId(randomInt, transactionWithoutId.amount());
        return creditWithTransactionId(transaction, token);
    }

    /**
     * Performs a debit transaction for a player without a specific transaction ID, generating a random ID.
     *
     * @param transactionWithoutId A {@code TransactionWithoutId} object containing transaction details.
     * @param token                The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> debitWithoutTransactionId(TransactionWithoutId transactionWithoutId, String token) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);
        TransactionWithId transaction = new TransactionWithId(randomInt, transactionWithoutId.amount());
        return debitWithTransactionId(transaction, token);
    }

    /**
     * Registers audit records for user actions.
     *
     * @param username   The name of the user for whom the audit is registered.
     * @param actionType The type of action being performed (e.g., REGISTRATION, AUTHORIZATION).
     * @param auditType  The type of audit result (e.g., SUCCESS, FAIL).
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
