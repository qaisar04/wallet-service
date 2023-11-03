package org.example.dto.transaction;

import java.math.BigDecimal;

public record TransactionWithoutId(BigDecimal amount) {

    @Override
    public BigDecimal amount() {
        return amount;
    }
}
