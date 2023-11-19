package org.example.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The transaction request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull
    private String playerUsername;

    @NotNull
    @DecimalMin(message = "Amount must not less than 0.0!", value = "0.0", inclusive = false)
    private BigDecimal amount;
}
