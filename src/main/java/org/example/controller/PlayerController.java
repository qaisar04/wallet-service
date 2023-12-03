package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.core.service.AuditService;
import org.example.core.service.PlayerService;
import org.example.core.service.TransactionService;
import org.example.dto.AuditDTO;
import org.example.dto.TransactionRequest;
import org.example.dto.TransactionResponse;
import org.example.mapper.TransactionMapper;
import org.example.security.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The {@code MyRestController} class is a Spring REST controller that provides a base functional API for various
 * operations, including player registration, viewing transaction history, and auditing. This API serves as the entry
 * point for these functionalities.
 *
 * <p>This controller includes the following main endpoints:
 * 1. `registerPlayer` - Allows the registration of a new player by providing player details.
 * 2. `viewTransactionHistory` - Retrieves the transaction history of a player, provided an authorization token.
 * 3. `viewAuditHistory` - Retrieves the audit history, which is accessible only after administrator authorization.
 *
 * <p>Example usage:
 * <pre>
 * // Use this controller in your Spring application to provide base functionality.
 * &lt;context:component-scan base-package="org.example.controller" /&gt;
 * </pre>
 * <p>
 * The controller is annotated with `@Loggable` for logging purposes and includes Swagger annotations for API documentation.
 */
@Tag(name = "Base functional API", description = "API for player registration, getting transaction history and all audits")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/players", produces = "application/json")
public class PlayerController {

    private final PlayerService playerService;
    private final JwtProvider jwtProvider;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final AuditService auditService;

    /**
     * Retrieves the balance of a player based on the provided authorization token.
     *
     * @param token An authentication token provided in the request header.
     * @return A ResponseEntity containing the player's balance.
     */
    @Operation(summary = "Method for viewing the balance")
    @GetMapping("/balance")
    public ResponseEntity<Map<String, String>> viewPlayerBalance(
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        token = token.substring(7);
        if (jwtProvider.isTokenExpired(token)) {
            response.put("error", "Incorrect login.");
            return ResponseEntity.badRequest()
                    .body(response);
        }

        String username = jwtProvider.extractUsername(token);
        Player player = playerService.findByUsername(username);
        response.put("balance", player.getBalance().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the transaction history of a player based on the provided authorization token.
     *
     * @param token An authentication token provided in the request header.
     * @return A ResponseEntity containing the transaction history of the player.
     */
    @Operation(summary = "Method for viewing transaction history")
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> viewTransactionHistory(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        if (jwtProvider.isTokenExpired(token)) {
            response.put("error", "Incorrect login.");
            return ResponseEntity.badRequest().body(response);
        }

        token = token.substring(7);
        String username = jwtProvider.extractUsername(token);
        Player player = playerService.findByUsername(username);
        List<TransactionResponse> list = transactionMapper.toDTOList(
                transactionService.getPlayerHistory(player.getId()));
        response.put("username", username);
        response.put("transactions", list);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the audit history, which is accessible only after administrator authorization.
     *
     * @param token An authentication token provided in the request header.
     * @return A ResponseEntity containing the audit history.
     */
    @Operation(summary = "Method for viewing audit history")
    @GetMapping("/audits")
    public ResponseEntity<Map<String, Object>> viewAuditHistory(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        token = token.substring(7);
        if (jwtProvider.isTokenExpired(token)) {
            response.put("error", "Access denied. Log in with admin account.");
            return ResponseEntity.badRequest().body(response);
        }

        List<AuditDTO> auditHistory = auditService.getAuditHistory();
        response.put("audits", auditHistory);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles credit transactions with a transaction ID. Receives a credit transaction request and an authentication token,
     * then returns a response with the result of the transaction, which may include an error message in case of failure.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details, including a transaction ID.
     * @param token      An authentication token provided in the request header.
     * @return A ResponseEntity containing the result of the credit transaction, including an error message if the transaction fails.
     */
    @Operation(summary = "Method for credit transaction")
    @PostMapping("/transactions/credit")
    public ResponseEntity<Map<String, String>> credit(
            @RequestBody TransactionRequest transaction,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        token = token.substring(7);
        if (!jwtProvider.validateToken(token, transaction.getUsername())) {
            response.put("error", "Incorrect login.");
            return ResponseEntity.badRequest()
                    .body(response);
        }

        Player player = playerService.findByUsername(transaction.getUsername());
        transactionService.credit(player.getId(), transaction.getAmount(), UUID.randomUUID());
        response.put("message", "Транзакция успешно завершена");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Method for debit transaction")
    @PostMapping("/transactions/debit")
    public ResponseEntity<Map<String, String>> debit(
            @RequestBody TransactionRequest transaction,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();

        token = token.substring(7);
        if (!jwtProvider.validateToken(token, transaction.getUsername())) {
            response.put("error", "Incorrect login.");
            return ResponseEntity.badRequest()
                    .body(response);
        }

        Player player = playerService.findByUsername(transaction.getUsername());
        transactionService.debit(player.getId(), transaction.getAmount(), UUID.randomUUID());
        response.put("message", "Транзакция успешно завершена");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> viewPlayerInfo(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        token = token.substring(7);
        if (jwtProvider.isTokenExpired(token)) {
            response.put("error", "Incorrect login.");
            return ResponseEntity.badRequest().body(response);
        }


        String username = jwtProvider.extractUsername(token);
        Player player = playerService.findByUsername(username);
        response.put("info", player);
        return ResponseEntity.ok(response);
    }







}
