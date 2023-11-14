package org.example.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    /**
     * The unique identifier of the transaction.
     */
    @Id
    @SequenceGenerator(name = "transaction_generator", sequenceName = "transaction_id_seq", allocationSize = 1, schema = "develop")
    @GeneratedValue(generator = "transaction_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "transaction_id")
    private Integer transactionId;

    /**
     * The type of the transaction (e.g., credit or debit).
     */
    @NotNull
    @Column(name = "type")
    private TransactionType type;

    /**
     * The monetary amount associated with the transaction.
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * The unique identifier of the player involved in the transaction.
     */
    @Column(name = "player_id")
    private Integer playerId;

    public static Transaction createTransaction(Integer transactionId, TransactionType type, BigDecimal amount, Integer playerId) {
        return Transaction.builder()
                .transactionId(transactionId)
                .type(type)
                .amount(amount)
                .playerId(playerId)
                .build();
    }
}

