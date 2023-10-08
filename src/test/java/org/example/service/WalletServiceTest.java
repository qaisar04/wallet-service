package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WalletServiceTest {

    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        walletService = new WalletService();
    }

    @Test
    public void testRegisterPlayer() {
        assertTrue(walletService.registerPlayer("newUser", "newPassword"));
        assertFalse(walletService.registerPlayer("admin", "admin"));
    }

    @Test
    public void testAuthenticatePlayer() {
        assertTrue(walletService.authenticatePlayer("admin", "admin"));
        assertFalse(walletService.authenticatePlayer("nonexistentUser", "password"));
    }

    @Test
    public void testGetBalance() {
        assertEquals(0.0, walletService.getBalance("admin"));
        assertEquals(-1.0, walletService.getBalance("nonexistentUser"));
    }

    @Test
    public void testDebit() throws Exception {
        assertTrue(walletService.registerPlayer("user1", "password1"));
        assertTrue(walletService.credit("user1", "txn1", 100.0));
        assertFalse(walletService.debit("user1", "txn1", 111.0));
        assertFalse(walletService.debit("user2", "txn1", 50.0));
    }

    @Test
    public void testCredit() {
        assertTrue(walletService.registerPlayer("user1", "password1"));
        walletService.credit("user1", "txn1", 100.0);
        assertEquals(100.0, walletService.getBalance("user1"));
    }

    @Test
    public void testDebitDuplicateTransactionId() throws Exception {
        assertTrue(walletService.registerPlayer("user1", "password1"));
        walletService.credit("user1", "txn1", 100.0);
        walletService.debit("user1", "txn2", 10.0);

        assertFalse(walletService.debit("user1", "txn1", 50.0));
        assertFalse(walletService.credit("user1","txn2",500));
    }


    @Test
    public void testViewTransactionHistory() {
        assertTrue(walletService.registerPlayer("user1", "password1"));
        walletService.credit("user1", "txn1", 100.0);

        assertEquals(1, walletService.viewTransactionHistory("user1"));
        assertEquals(0, walletService.viewTransactionHistory("nonexistentUser"));
    }

    @Test
    public void testViewAllAudits() throws Exception {
        assertTrue(walletService.registerPlayer("user1", "password1"));
        walletService.authenticatePlayer("user1", "password1");
        walletService.credit("user1", "txn1", 50.0);

        assertEquals(3, walletService.viewAllAudits());
    }
}
