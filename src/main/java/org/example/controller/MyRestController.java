package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
@RestController
@Api(value = "Base functional API", description = "API for player registration, getting transaction history and all audits")
@RequestMapping(value = "/api", produces = "application/json")
    public class MyRestController {

    private final TransactionManager transactionManager;
    private final PlayerManager playerManager;

    @Autowired
    public MyRestController(TransactionManager transactionManager, PlayerManager playerManager) {
        this.transactionManager = transactionManager;
        this.playerManager = playerManager;
    }

    /**
     * Handles player registration by receiving player details and returning a response containing the registered player.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player registration details.
     * @return A ResponseEntity containing the registered player.
     */
    @Loggable
    @ApiOperation(value = "post method for register", response = Player.class)
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
    @Loggable
    @ApiOperation(value = "post method for viewing player balance, works only after authorization", response = BigDecimal.class)
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
    @Loggable
    @ApiOperation(value = "post method for viewing all transactions for a certain player, works only after authorization", response = HashMap.class)
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
    @Loggable
    @ApiOperation(value = "post method for viewing all audits, works only after administrator authorization", response = HashMap.class)
    @GetMapping("/audits")
    public ResponseEntity<HashMap<String, Object>> viewAuditHistory(
            @RequestHeader("Authorization") String token) {
        return transactionManager.viewAllAudits(token);
    }
}

