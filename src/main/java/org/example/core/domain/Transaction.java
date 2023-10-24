package org.example.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.types.TransactionType;

import java.math.BigDecimal;

/**
 * The `Transaction` class represents a financial transaction within the system.
 * It contains information about the transaction, including its unique identifier,
 * custom identifier, type, amount, and the player's ID involved in the transaction.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    /**
     * The unique identifier of the transaction.
     */
    private Integer transactionId;

    /**
     * The type of the transaction (e.g., credit or debit).
     */
    private TransactionType type;

    /**
     * The monetary amount associated with the transaction.
     */
    private BigDecimal amount;

    /**
     * The unique identifier of the player involved in the transaction.
     */
    private Integer playerId;

    /**
     * Creates a new transaction with the specified parameters.
     *
     * @param transactionId The custom identifier for the transaction.
     * @param type The type of the transaction (e.g., credit or debit).
     * @param amount The monetary amount associated with the transaction.
     * @param playerId The unique identifier of the player involved in the transaction.
     * @return A new `Transaction` object with the specified parameters.
     */
    public static Transaction createTransaction(Integer transactionId, TransactionType type, BigDecimal amount, Integer playerId) {
        return Transaction.builder()
                .transactionId(transactionId)
                .type(type)
                .amount(amount)
                .playerId(playerId)
                .build();
    }
}

