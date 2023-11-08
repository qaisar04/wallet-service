package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.logging.aop.annotations.LoggableInfo;
import org.example.manager.PlayerManagerImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The {@code DebitTransactionController} class is a Spring REST controller responsible for handling debit transaction
 * requests. It provides endpoints for making debit transactions, both with and without a transaction ID.
 *
 * <p>This controller is part of the API for debit transactions, allowing players to perform debit transactions.
 *
 * <p>Example usage:
 * <pre>
 * // Use this controller in your Spring application to handle debit transactions.
 * &lt;context:component-scan base-package="org.example.controller" /&gt;
 * </pre>
 *
 * The controller provides two main endpoints:
 * 1. `debitTransactionWithTransactionId` - Receives a debit transaction request with a transaction ID and returns
 *    a response, which can include an error message if the transaction fails.
 * 2. `debitTransactionWithoutTransactionId` - Receives a debit transaction request without a transaction ID and
 *    returns a response, which can include an error message if the transaction fails.
 *
 * Both endpoints are annotated with the `@Loggable` annotation for logging purposes.
 */
@Tag(name = "API for debit transaction", description = "this API allows you to make a debit transaction")
@LoggableInfo
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/debit", produces = "application/json")
public class DebitTransactionController {

    private final PlayerManagerImpl playerManager;

    /**
     * Handles debit transactions with a transaction ID. Receives a debit transaction request and an authentication token,
     * then returns a response with the result of the transaction, which may include an error message in case of failure.
     *
     * @param transaction A {@code TransactionWithId} object containing transaction details, including a transaction ID.
     * @param token      An authentication token provided in the request header.
     * @return A ResponseEntity containing the result of the debit transaction, including an error message if the transaction fails.
     */
    @Operation(summary = "Method for debit transaction using ID")
    @PostMapping("/id")
    public ResponseEntity<Map<String, String>> debitTransactionWithTransactionId(
            @RequestBody TransactionWithId transaction,
            @RequestHeader("Authorization") String token) {
            return playerManager.debitWithTransactionId(transaction, token);
    }

    /**
     * Handles debit transactions without a transaction ID. Receives a debit transaction request and an authentication token,
     * then returns a response with the result of the transaction, which may include an error message in case of failure.
     *
     * @param transaction A {@code TransactionWithoutId} object containing transaction details.
     * @param token      An authentication token provided in the request header.
     * @return A ResponseEntity containing the result of the debit transaction, including an error message if the transaction fails.
     */
    @Operation(summary = "Method for debit transaction without using ID")
    @PostMapping()
    public ResponseEntity<Map<String, String>> debitTransactionWithoutTransactionId(
            @RequestBody TransactionWithoutId transaction,
            @RequestHeader("Authorization") String token) {
            return playerManager.debitWithoutTransactionId(transaction, token);
    }
}


