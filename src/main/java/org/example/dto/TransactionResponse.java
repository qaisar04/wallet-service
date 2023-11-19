package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The transaction response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String type;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal amount;
    private UUID transactionIdentifier;
}