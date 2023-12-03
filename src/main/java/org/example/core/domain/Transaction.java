package org.example.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.types.TransactionType;
import org.example.util.UuidConverter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The `Transaction` class represents a financial transaction within the system.
 * It contains information about the transaction, including its unique identifier,
 * custom identifier, type, amount, and the player's ID involved in the transaction.
 */
@Data
@Builder
@Entity
@Table(name = "transactions", schema = "wallet")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    /**
     * The unique identifier of the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long transactionId;

    /**
     * The type of the transaction (e.g., credit or debit).
     */
    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * The monetary amount associated with the transaction.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance_before")
    private BigDecimal balanceBefore;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    @Convert(converter = UuidConverter.class)
    @Column(name = "transaction_identifier", unique = true)
    private UUID transactionIdentifier;

    /**
     * The unique identifier of the player involved in the transaction.
     */
    @Column(name = "player_id")
    private Long playerId;
}

