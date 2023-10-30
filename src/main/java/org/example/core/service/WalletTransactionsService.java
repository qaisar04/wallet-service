package org.example.core.service;

import org.example.core.domain.Transaction;
import org.example.dao.impl.TransactionDaoImpl;
import java.util.List;
import java.util.Optional;

/**
 * Класс WalletTransactionsService представляет собой сервис для управления транзакциями.
 * Он позволяет выполнять дебетовые и кредитные транзакции,  а также просматривать историю транзакций.
 */
public class WalletTransactionsService implements Service<Integer, Transaction> {

    private final TransactionDaoImpl transactionDaoImpl = TransactionDaoImpl.getInstance();

    private static WalletTransactionsService walletTransactionsService = new WalletTransactionsService();

    public List<Transaction> findByPlayerId(Integer id) {
        return transactionDaoImpl.findByPlayerId(id);
    }

    public Optional<Transaction> findById(Integer id) {
        return transactionDaoImpl.findById(id);
    }

    public List<Transaction> findAll() {
        return transactionDaoImpl.findAll();
    }

    public void save(Transaction transaction) {
        transactionDaoImpl.save(transaction);
    }

    public void update(Transaction transaction) {
        transactionDaoImpl.update(transaction);
    }

    public boolean delete(Integer id) {
        return transactionDaoImpl.delete(id);
    }

    public boolean deleteAll() {
        return transactionDaoImpl.deleteAll();
    }

    public static WalletTransactionsService getInstance() {
        return walletTransactionsService;
    }

}
