package org.example.serviceTest;

import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.domain.types.TransactionType;
import org.example.dao.impl.PlayerDaoImpl;
import org.example.dao.impl.TransactionDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDaoImplTest {
    private TransactionDaoImpl transactionDao;
    private PlayerDaoImpl playerDao;
    private Transaction testTransaction;
    private Player testPlayer;

    // я не успел сделать тест контейнеры

    @BeforeEach
    public void setUp() {
        transactionDao = TransactionDaoImpl.getInstance();
        playerDao = PlayerDaoImpl.getInstance();
        transactionDao.deleteAll();
        playerDao.deleteAll();

        testPlayer = Player.builder()
                .fullName("TestPlayer")
                .password("password")
                .balance(BigDecimal.valueOf(100.0))
                .build();
        playerDao.save(testPlayer);

        testTransaction = Transaction.builder()
                .customId(12345)
                .type(TransactionType.CREDIT)
                .amount(BigDecimal.valueOf(50.0))
                .playerId(testPlayer.getId())
                .build();
    }

    @Test
    public void testFindById() {
        transactionDao.save(testTransaction);

        Optional<Transaction> foundTransaction = transactionDao.findById(testTransaction.getId());
        assertTrue(foundTransaction.isPresent());
        assertNotEquals(testTransaction.getId(), foundTransaction.get().getId());
        assertEquals(testTransaction.getCustomId(), foundTransaction.get().getCustomId());
    }

    @Test
    public void testFindByCustomId() {
        // Вставляем тестовую транзакцию в базу данных
        transactionDao.save(testTransaction);

        Optional<Transaction> foundTransaction = transactionDao.findByCustomId(testTransaction.getCustomId());
        assertTrue(foundTransaction.isPresent());
        assertEquals(testTransaction.getCustomId(), foundTransaction.get().getCustomId());
    }

    @Test
    public void testFindByPlayerId() {
        transactionDao.save(testTransaction);
        List<Transaction> foundTransactions = transactionDao.findByPlayerId(testPlayer.getId());
        assertFalse(foundTransactions.isEmpty());
    }

    @Test
    public void testFindAll() {
        transactionDao.save(testTransaction);
        List<Transaction> transactions = transactionDao.findAll();
        assertFalse(transactions.isEmpty());
    }

    @Test
    public void testUpdate() {
        transactionDao.save(testTransaction);

        testTransaction.setType(TransactionType.DEBIT);
        testTransaction.setAmount(BigDecimal.valueOf(25.55));
        transactionDao.update(testTransaction);
        Optional<Transaction> updatedTransaction = transactionDao.findById(testTransaction.getId());
        assertTrue(updatedTransaction.isPresent());
        assertEquals(TransactionType.DEBIT, updatedTransaction.get().getType());
        assertEquals(BigDecimal.valueOf(25.55), updatedTransaction.get().getAmount());
    }

    @Test
    public void testDelete() {
        // Вставляем тестовую транзакцию в базу данных
        transactionDao.save(testTransaction);

        assertTrue(transactionDao.delete(testTransaction.getId()));

        Optional<Transaction> deletedTransaction = transactionDao.findById(testTransaction.getId());
        assertFalse(deletedTransaction.isPresent());
    }

    @Test
    public void testDeleteAll() {
        // Вставляем тестовую транзакцию в базу данных
        transactionDao.save(testTransaction);

        boolean deleted = transactionDao.deleteAll();
        assertTrue(deleted);

        List<Transaction> transactions = transactionDao.findAll();
        assertTrue(transactions.isEmpty());
    }
}
