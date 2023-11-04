package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.core.domain.types.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * The `Transaction` class represents a financial transaction within the system.
 * It contains information about the transaction, including its unique identifier,
 * custom identifier, type, amount, and the player's ID involved in the transaction.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    /**
     * The unique identifier of the player involved in the transaction.
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
}

