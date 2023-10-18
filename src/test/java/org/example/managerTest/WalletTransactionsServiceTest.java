package org.example.managerTest;

import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WalletTransactionsServiceTest {


    private TransactionManager transactionManager;
    private PlayerManager playerManager;

    WalletPlayerService walletPlayerService = WalletPlayerService.getInstance();
    WalletTransactionsService walletTransactionsService = WalletTransactionsService.getInstance();
    WalletAuditService walletAuditService = WalletAuditService.getInstance();


    @BeforeEach
    public void setUp() {
        walletTransactionsService.deleteAll();
        walletAuditService.deleteAll();
        walletPlayerService.deleteAll();

        playerManager = new PlayerManager();
        transactionManager = new TransactionManager(playerManager);
    }

    @Test
    public void testViewTransactionHistory() {
        assertTrue(playerManager.registerPlayer("bob", "password1"));
        playerManager.creditWithTransactionId("bob", 6453452, BigDecimal.valueOf(100));
        assertEquals(1, transactionManager.viewTransactionHistory("bob"));
        assertEquals(0, transactionManager.viewTransactionHistory("nonexistentUser"));
    }

    @Test
    public void testViewAllAudits() throws Exception {
        assertTrue(playerManager.registerPlayer("user1", "password1"));
        playerManager.authenticatePlayer("user1", "password1");
        playerManager.creditWithoutTransactionId("user1", BigDecimal.valueOf(50.0));
        assertEquals(3, transactionManager.viewAllAudits());
    }
}
