package org.example.repository;

import org.example.core.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPlayerId(Long playerId);

    Optional<Transaction> findByTransactionId(UUID transactionIdentifier);

}
