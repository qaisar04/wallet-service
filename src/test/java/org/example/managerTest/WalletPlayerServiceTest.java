package org.example.managerTest;

import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class WalletPlayerServiceTest {
    private PlayerManager playerManager = new PlayerManager();
    private TransactionManager transactionManager = new TransactionManager();
//    private TransactionManager transactionManager = new TransactionManager(playerManager);

    WalletPlayerService walletPlayerService = WalletPlayerService.getInstance();
    WalletTransactionsService walletTransactionsService = WalletTransactionsService.getInstance();
    WalletAuditService walletAuditService = WalletAuditService.getInstance();

    @BeforeEach
    public void setUp() {
        walletTransactionsService.deleteAll();
        walletAuditService.deleteAll();
        walletPlayerService.deleteAll();
        playerManager = new PlayerManager();
    }

    @Test
    public void testRegisterPlayer() {
        assertTrue(playerManager.registerPlayer("newUser", "newPassword"));
        assertTrue(playerManager.registerPlayer("admin", "admin"));
        assertFalse(playerManager.registerPlayer("newUser", "newPassword"));
    }

    @Test
    public void testAuthenticatePlayer() {
        assertFalse(playerManager.authenticatePlayer("admin", "admin"));
        assertTrue(playerManager.registerPlayer("admin", "admin"));
        assertTrue(playerManager.authenticatePlayer("admin", "admin"));
        assertFalse(playerManager.authenticatePlayer("nonexistentUser", "password"));
    }

    @Test
    public void testGetBalance() {
        assertEquals(BigDecimal.valueOf(-1.0), playerManager.getBalance("admin"));
        assertEquals(BigDecimal.valueOf(-1.0), playerManager.getBalance("nonexistentUser"));
    }

    @Test
    public void testCreditWithoutTransactionId() {
        assertTrue(playerManager.registerPlayer("user1", "password1"));
        playerManager.creditWithoutTransactionId("user1", BigDecimal.valueOf(150.0));
        assertFalse(playerManager.creditWithoutTransactionId("user2", BigDecimal.valueOf(100.0)));

        BigDecimal expectedBalance = BigDecimal.valueOf(150.0);
        BigDecimal actualBalance = playerManager.getBalance("user1");

        assertEquals(expectedBalance.setScale(2, RoundingMode.HALF_UP), actualBalance.setScale(2, RoundingMode.HALF_UP));
    }


    @Test
    public void testDebitWithoutTransactionId() {
        assertTrue(playerManager.registerPlayer("user1", "password1"));
        assertTrue(playerManager.creditWithoutTransactionId("user1", BigDecimal.valueOf(100.0)));
        assertTrue(playerManager.debitWithoutTransactionId("user1", BigDecimal.valueOf(100.0)));
        assertFalse(playerManager.debitWithoutTransactionId("user2", BigDecimal.valueOf(100.0)));
    }

    @Test
    public void testCreditWithTransactionId() {
        assertTrue(playerManager.registerPlayer("user1", "password1"));
        playerManager.creditWithTransactionId("user1", 101299441, BigDecimal.valueOf(100.0));
        assertFalse(playerManager.creditWithTransactionId("user2", 101030344, BigDecimal.valueOf(100.0)));
        assertTrue(playerManager.creditWithTransactionId("user1", 1012001941, BigDecimal.valueOf(100.0)));
        BigDecimal expectedBalance = BigDecimal.valueOf(200.0);
        BigDecimal actualBalance = playerManager.getBalance("user1");


        assertEquals(expectedBalance.setScale(2, RoundingMode.HALF_UP), actualBalance.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testDebitWithTransactionId() {
        assertTrue(playerManager.registerPlayer("user1", "password1"));
        assertTrue(playerManager.creditWithoutTransactionId("user1", BigDecimal.valueOf(500.0)));
        assertTrue(playerManager.debitWithTransactionId("user1", 102441924, BigDecimal.valueOf(111.0)));
        assertFalse(playerManager.debitWithTransactionId("user1", 30521, BigDecimal.valueOf(1000.0)));
        assertFalse(playerManager.debitWithoutTransactionId("user2", BigDecimal.valueOf(50.0)));
        assertEquals(5, transactionManager.viewAllAudits());
    }


}
