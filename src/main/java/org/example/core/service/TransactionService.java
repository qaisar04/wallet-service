package org.example.core.service;

import org.example.core.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    /**
     * Get the player's transactions history
     *
     * @param playerId the player id
     * @return the player's list of transactions
     */
    List<Transaction> getPlayerHistory(Long playerId);

    /**
     * Debit transaction process
     *
     * @param amount the amount of transaction
     * @param transactionIdentifier the transaction identifier
     * @param playerId the player id
     */
    void debit(Long playerId, BigDecimal amount, UUID transactionIdentifier);

    /**
     * Credit transaction process
     *
     * @param amount the amount of transaction
     * @param transactionIdentifier the transaction identifier
     * @param playerId the player id
     */
    void credit(Long playerId, BigDecimal amount, UUID transactionIdentifier);

}
