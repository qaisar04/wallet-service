package org.example.dto.transaction;

import lombok.Setter;

import java.math.BigDecimal;

public record TransactionWithId(Integer id,
                                BigDecimal amount) {

}
