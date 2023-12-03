package org.example.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.Transaction;
import org.example.core.domain.types.TransactionType;
import org.example.core.service.PlayerService;
import org.example.core.service.TransactionService;
import org.example.dto.TransactionResponse;
import org.example.exception.TransactionAlreadyExistsException;
import org.example.exception.TransactionOperationException;
import org.example.mapper.TransactionMapper;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> getPlayerHistory(Long playerId) {
        return transactionRepository.findAllByPlayerId(playerId);

    }

    @Transactional
    @Override
    public void credit(Long playerId, BigDecimal amount, UUID transactionIdentifier) {
        checkTransaction(transactionIdentifier);

        Transaction transaction = openNewTransaction(TransactionType.CREDIT, playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        transaction.setAmount(amount);
        transaction.setBalanceBefore(playerBalance);
        transaction.setTransactionIdentifier(transactionIdentifier);

        BigDecimal result = playerBalance.add(amount);
        transaction.setBalanceAfter(result);

        transactionRepository.save(transaction);
        playerService.updateBalance(playerId, result);
    }

    @Override
    @Transactional
    public void debit(Long playerId, BigDecimal amount, UUID transactionIdentifier) {
        checkTransaction(transactionIdentifier);

        Transaction transaction = openNewTransaction(TransactionType.DEBIT, playerId);
        BigDecimal playerBalance = playerService.getPlayerBalance(playerId);

        if (playerBalance.compareTo(amount) < 0) {
            throw new TransactionOperationException("Insufficient funds.");
        }

        transaction.setAmount(amount);
        transaction.setBalanceBefore(playerBalance);
        transaction.setTransactionIdentifier(transactionIdentifier);

        BigDecimal result = playerBalance.subtract(amount);
        transaction.setBalanceAfter(result);

        transactionRepository.save(transaction);
        playerService.updateBalance(playerId, result);
    }

    private Transaction openNewTransaction(TransactionType type, Long playerId) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setPlayerId(playerId);
        return transaction;
    }

    private void checkTransaction(UUID transactionIdentifier) {
        if (transactionRepository.findByTransactionIdentifier(transactionIdentifier).isPresent()) {
            throw new TransactionAlreadyExistsException("Transaction with ID " + transactionIdentifier + " already exists.");
        }
    }
}
