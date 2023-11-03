package org.example.manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.PlayerDto;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.TransactionException;
import org.example.util.JwtUtils;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

import static org.example.core.domain.types.ActionType.*;
import static org.example.core.domain.types.AuditType.*;
import static org.example.core.domain.types.TransactionType.*;
import static org.example.core.domain.types.TransactionType.CREDIT;


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
     * The method registers a new player with the specified name and password.
     *
     * @return true if registration is successful, otherwise false.
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
     * The method authenticates the player by username and password.
     *
     * @return true if authentication is successful, otherwise false.
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
     * The method returns the balance of the player with the specified name.
     *
     * @param username The player's name.
     * @return The player's balance.
     */
    public BigDecimal getBalance(String username) {
        Player player = playerService.findByUsername(username).orElse(null);
        if (player != null) {
            audit(username, BALANCE_INQUIRY, SUCCESS);
            return player.getBalance();
        } else {
            audit(username, BALANCE_INQUIRY, FAIL);
            return BigDecimal.valueOf(-1.0);
        }
    }

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

            if (!optionalPlayer.isPresent()) {
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

            if (!optionalPlayer.isPresent()) {
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


    public ResponseEntity<Map<String, String>> creditWithoutTransactionId(TransactionWithoutId transactionWithoutId, String token) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);
        TransactionWithId transaction = new TransactionWithId(randomInt, transactionWithoutId.amount());
        return creditWithTransactionId(transaction, token);
    }

    public ResponseEntity<Map<String, String>> debitWithoutTransactionId(TransactionWithoutId transactionWithoutId, String token) {
        Random random = new Random();
        long randomLong = random.nextLong();
        int randomInt = (int) (randomLong & 0x7FFFFFFF);
        TransactionWithId transaction = new TransactionWithId(randomInt, transactionWithoutId.amount());
        return debitWithTransactionId(transaction, token);
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
