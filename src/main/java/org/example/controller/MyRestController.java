package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.logging.aop.annotations.LoggableInfo;
import org.example.manager.PlayerManagerImpl;
import org.example.manager.TransactionManager;
import org.example.wrapper.PlayerWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
 *
 * The controller is annotated with `@Loggable` for logging purposes and includes Swagger annotations for API documentation.
 */
@Tag(name = "Base functional API", description = "API for player registration, getting transaction history and all audits")
@RestController
@LoggableInfo
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = "application/json")
    public class MyRestController {

    private final TransactionManager transactionManager;
    private final PlayerManagerImpl playerManager;

    /**
     * Handles player registration by receiving player details and returning a response containing the registered player.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player registration details.
     * @return A ResponseEntity containing the registered player.
     */
    @Operation(summary = "Method for user registration")
    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody PlayerWrapper playerWrapper) {
        return playerManager.registerPlayer(playerWrapper);
    }

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
        return playerManager.getBalance(token);
    }


    /**
     * Retrieves the transaction history of a player based on the provided authorization token.
     *
     * @param token An authentication token provided in the request header.
     * @return A ResponseEntity containing the transaction history of the player.
     */
    @Operation(summary = "Method for viewing transaction history")
    @GetMapping("/history")
    public ResponseEntity<HashMap<String, Object>> viewTransactionHistory(
            @RequestHeader("Authorization") String token) {
        return transactionManager.viewTransactionHistory(token);
    }

    /**
     * Retrieves the audit history, which is accessible only after administrator authorization.
     *
     * @param token An authentication token provided in the request header.
     * @return A ResponseEntity containing the audit history.
     */
    @Operation(summary = "Method for viewing audit history")
    @GetMapping("/audits")
    public ResponseEntity<HashMap<String, Object>> viewAuditHistory(
            @RequestHeader("Authorization") String token) {
        return transactionManager.viewAllAudits(token);
    }
}

