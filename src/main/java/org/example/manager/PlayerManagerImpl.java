package org.example.manager;

import io.jsonwebtoken.Claims;
import kz.baltabayev.audits.manager.PlayerManager;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.BadRequestException;
import org.example.exception.PlayerNotFoundException;
import org.example.exception.UnauthorizedAccessException;
import org.example.logging.aop.annotations.LoggableTime;
import org.example.util.JwtUtils;
import org.example.wrapper.PlayerWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

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
 * <p>
 * The class is annotated with `@Component` for Spring component scanning.
 */
@Component
@LoggableTime
@RequiredArgsConstructor
public class PlayerManagerImpl implements PlayerManager<Player, PlayerWrapper, TransactionWithId, TransactionWithoutId> {

    private final WalletPlayerService playerService;
    private final WalletTransactionsService transactionsService;
    private final JwtUtils jwtUtils;


    /**
     * Registers a new player with the specified username and password.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player registration details.
     * @return A ResponseEntity containing the registered player.
     */
    public ResponseEntity<Player> registerPlayer(PlayerWrapper playerWrapper) {
        Player player = new Player(playerWrapper.getUsername(), playerWrapper.getPassword());
        Optional<Player> existingPlayer = playerService.findByUsername(player.getUsername());

        try {
            if (existingPlayer.isEmpty()) {
                Player newPlayer = Player.builder()
                        .username(player.getUsername())
                        .password(player.getPassword())
                        .balance(BigDecimal.ZERO)
                        .build();
                playerService.save(newPlayer);
                return ResponseEntity.ok(newPlayer);
            } else {
                throw new Exception("Registration audit failed for user: " + player.getUsername());
            }
        } catch (Exception e) {
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
        try {
            Optional<Player> optionalPlayer = Optional.ofNullable(playerService.findByUsername(player.getUsername()).orElse(null));
            if (optionalPlayer.isPresent() && optionalPlayer.get().getPassword().equals(player.getPassword())) {

                String jwt = jwtUtils.generateToken(optionalPlayer.get());

                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("message", "Authentication is successful");
                responseMap.put("jwt", jwt);
                return ResponseEntity.ok(responseMap);
            } else {
                throw new Exception("Authentication audit failed for user: " + playerWrapper.getUsername());
            }
        } catch (Exception e) {
            Map<String, String> errorMap = Map.of("error", "Authentication is failed");
            return ResponseEntity.badRequest().body(errorMap);
        }
    }

    /**
     * Retrieves the balance of the player with the specified username.
     *
     * @param token The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> getBalance(String token) {
        Map<String, String> response = new HashMap<>();

        try {
            if (!token.startsWith("Bearer ")) {
                throw new UnauthorizedAccessException("Несанкционированный доступ");
            }

            String username = getUsernameFromToken(token);

            if (username != null) {
                Player player = playerService.findByUsername(username).orElse(null);
                if (player != null) {
                    String balance = String.valueOf(player.getBalance());
                    response.put("balance", balance);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("error", "Игрок не найден в базе данных");
                    throw new PlayerNotFoundException("Игрок не найден в базе данных");
                }
            }
            throw new UnauthorizedAccessException("Неавторизованный пользователь");
        } catch (UnauthorizedAccessException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            String username = getUsernameFromToken(token);
            response.put("error", "Ошибка при обработке запроса");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getPayload(token);
            return (String) claims.get("username");
        }
        return null;
    }


    /**
     * Performs a credit transaction for a player using a transaction ID, checking for authorization and validation.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details.
     * @param token       The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> creditWithTransactionId(TransactionWithId transaction, String token) {
        Map<String, String> response = new HashMap<>();

        if (!token.startsWith("Bearer ")) {
            response.put("error", "Несанкционированный доступ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String username = getUsernameFromToken(token);

        if (token != null && username != null) {
            token = token.substring(7);

            if (transaction.id() <= 0 || transaction.amount().compareTo(BigDecimal.ZERO) <= 0) {
                response.put("error", "Неверные детали транзакции");
                throw new BadRequestException("Invalid transaction details");
            }

            Optional<Player> optionalPlayer = playerService.findByUsername(username);

            if (optionalPlayer.isEmpty()) {
                response.put("error", "Игрок не найден");
                throw new BadRequestException("Player not found");
            }

            Player player = optionalPlayer.get();

            if (!optionalPlayer.get().getUsername().equals(username)) {
                throw new BadRequestException("Credit transaction failure");
            }

            Transaction successTransaction = Transaction.createTransaction(transaction.id(), CREDIT, transaction.amount(), player.getId());
            player.setBalance(player.getBalance().add(transaction.amount()));

            Optional<Transaction> existingTransaction = transactionsService.findById(transaction.id());

            if (existingTransaction.isPresent()) {
                throw new BadRequestException("Transaction already exists");
            }

            transactionsService.save(successTransaction);
            playerService.update(player);
            response.put("message", "Транзакция успешно завершена");
            return ResponseEntity.ok(response);
        }
        response.put("error", "Неавторизованный пользователь");
        throw new UnauthorizedAccessException("Unauthorized access");
    }



    /**
     * Performs a debit transaction for a player using a transaction ID, checking for authorization and validation.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details.
     * @param token       The JWT token for authorization.
     * @return A ResponseEntity containing the transaction result.
     */
    public ResponseEntity<Map<String, String>> debitWithTransactionId(TransactionWithId transaction, String token) {
        Map<String, String> response = new HashMap<>();

        if (!token.startsWith("Bearer ")) {
            response.put("error", "Несанкционированный доступ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String username = getUsernameFromToken(token);

        if (token != null && username != null) {
            token = token.substring(7);

            if (transaction.id() <= 0 || transaction.amount().compareTo(BigDecimal.ZERO) <= 0) {
                response.put("error", "Неверные детали транзакции");
                throw new BadRequestException("Invalid transaction details");
            }

            Optional<Player> optionalPlayer = playerService.findByUsername(username);

            if (optionalPlayer.isEmpty()) {
                response.put("error", "Игрок не найден");
                throw new BadRequestException("Player not found");
            }

            Player player = optionalPlayer.get();

            if (!optionalPlayer.get().getUsername().equals(username)) {
                throw new BadRequestException("Debit transaction failure");
            }

            Transaction successTransaction = Transaction.createTransaction(transaction.id(), DEBIT, transaction.amount(), player.getId());
            if (player.getBalance().compareTo(transaction.amount()) < 0) {
                response.put("error", "Сбой дебетной транзакции, недостаточно средств для выполнения транзакции.");
                throw new BadRequestException("Insufficient balance for debit transaction");
            }

            Optional<Transaction> existingTransaction = transactionsService.findById(transaction.id());

            if (existingTransaction.isPresent()) {
                throw new BadRequestException("Transaction already exists");
            }

            player.setBalance(player.getBalance().subtract(transaction.amount()));
            transactionsService.save(successTransaction);
            playerService.update(player);
            response.put("message", "Транзакция успешно завершена");
            return ResponseEntity.ok(response);
        }
        response.put("error", "Неавторизованный пользователь");
        throw new UnauthorizedAccessException("Unauthorized access");
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
}
