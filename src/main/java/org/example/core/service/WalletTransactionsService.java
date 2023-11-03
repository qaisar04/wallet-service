package org.example.core.service;

import org.example.core.domain.Transaction;
import org.example.dao.impl.TransactionDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Класс WalletTransactionsService представляет собой сервис для управления транзакциями.
 * Он позволяет выполнять дебетовые и кредитные транзакции,  а также просматривать историю транзакций.
 */
@Service
public class WalletTransactionsService implements WalletService<Integer, Transaction> {

    private TransactionDaoImpl transactionDaoImpl;

    @Autowired
    public WalletTransactionsService(TransactionDaoImpl transactionDaoImpl) {
        this.transactionDaoImpl = transactionDaoImpl;
    }

    @Transactional
    public List<Transaction> findByPlayerId(Integer id) {
        return transactionDaoImpl.findByPlayerId(id);
    }

    @Override
    @Transactional
    public Optional<Transaction> findById(Integer id) {
        return transactionDaoImpl.findById(id);
    }

    @Override
    @Transactional
    public List<Transaction> findAll() {
        return transactionDaoImpl.findAll();
    }

    @Override
    @Transactional
    public void save(Transaction transaction) {
        transactionDaoImpl.save(transaction);
    }

    @Override
    @Transactional
    public void update(Transaction transaction) {
        transactionDaoImpl.update(transaction);
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        return transactionDaoImpl.delete(id);
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        return transactionDaoImpl.deleteAll();
    }

    private WalletTransactionsService() { }
}
