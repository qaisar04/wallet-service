package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WalletPlayerServiceTest {
    private WalletPlayerService walletPlayerService;

    @BeforeEach
    public void setUp() {
        walletPlayerService = new WalletPlayerService();
    }

    @Test
    public void testRegisterPlayer() {
        assertTrue(walletPlayerService.registerPlayer("newUser", "newPassword"));
        assertFalse(walletPlayerService.registerPlayer("admin", "admin"));
        assertFalse(walletPlayerService.registerPlayer("newUser", "newPassword"));
    }

    @Test
    public void testAuthenticatePlayer() {
        assertTrue(walletPlayerService.authenticatePlayer("admin", "admin"));
        assertFalse(walletPlayerService.authenticatePlayer("nonexistentUser", "password"));
    }

    @Test
    public void testGetBalance() {
        assertEquals(BigDecimal.valueOf(0.0), walletPlayerService.getBalance("admin"));
        assertEquals(BigDecimal.valueOf(-1.0), walletPlayerService.getBalance("nonexistentUser"));
    }

    @Test
    public void testCreditWithoutTransactionId() {
        assertTrue(walletPlayerService.registerPlayer("user1", "password1"));
        walletPlayerService.creditWithoutTransactionId("user1", BigDecimal.valueOf(100.0));
        assertFalse(walletPlayerService.creditWithoutTransactionId("user2", BigDecimal.valueOf(100.0)));
        assertEquals(BigDecimal.valueOf(100.0), walletPlayerService.getBalance("user1"));
    }

    @Test
    public void testDebitWithoutTransactionId() {
        assertTrue(walletPlayerService.registerPlayer("user1", "password1"));
        assertTrue(walletPlayerService.creditWithoutTransactionId("user1", BigDecimal.valueOf(100.0)));
        assertTrue(walletPlayerService.debitWithoutTransactionId("user1", BigDecimal.valueOf(100.0)));
        assertFalse(walletPlayerService.debitWithoutTransactionId("user2", BigDecimal.valueOf(100.0)));
    }

    @Test
    public void testCreditWithTransactionId() {
        assertTrue(walletPlayerService.registerPlayer("user1", "password1"));
        walletPlayerService.creditWithTransactionId("user1", "1012994912041", BigDecimal.valueOf(100.0));
        assertFalse(walletPlayerService.creditWithTransactionId("user2", "10103039144", BigDecimal.valueOf(100.0)));
        assertFalse(walletPlayerService.creditWithTransactionId("user1", "1012994912041", BigDecimal.valueOf(100.0)));
        assertEquals(BigDecimal.valueOf(100.0), walletPlayerService.getBalance("user1"));
    }

    @Test
    public void testDebitWithTransactionId() {
        assertTrue(walletPlayerService.registerPlayer("user1", "password1"));
        assertTrue(walletPlayerService.creditWithoutTransactionId("user1", BigDecimal.valueOf(500.0)));
        assertTrue(walletPlayerService.debitWithTransactionId("user1", "102495841924", BigDecimal.valueOf(111.0)));
        assertFalse(walletPlayerService.debitWithTransactionId("user1", "2030521", BigDecimal.valueOf(1000.0)));
        assertFalse(walletPlayerService.debitWithTransactionId("user2", "2194018248151", BigDecimal.valueOf(50.0)));
        assertEquals(5, walletPlayerService.getAudits().size());
    }


}
