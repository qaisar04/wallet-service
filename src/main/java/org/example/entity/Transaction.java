package org.example.entity;

import lombok.Data;
import org.example.types.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Класс, представляющий транзакцию.
 */
@Data
public class Transaction {
    private String id;
    private BigDecimal amount;
    private TransactionType type;

    /**
     * Конструктор для создания объекта Transaction.
     *
     * @param id     Уникальный идентификатор транзакции.
     * @param type   Тип транзакции ("debit" или "credit").
     * @param amount Сумма транзакции.
     */
    public Transaction(String id, TransactionType type, BigDecimal amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }
}

