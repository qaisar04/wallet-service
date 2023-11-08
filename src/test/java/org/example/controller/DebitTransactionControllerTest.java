package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.DebitTransactionController;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.TransactionException;
import org.example.manager.PlayerManagerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DebitTransactionControllerTest {

    private DebitTransactionController debitTransactionController;
    private PlayerManagerImpl playerManager;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        playerManager = mock(PlayerManagerImpl.class);
        debitTransactionController = new DebitTransactionController(playerManager);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDebitTransactionWithTransactionId_Failure() throws Exception {
        TransactionWithId transaction = new TransactionWithId(123, BigDecimal.valueOf(100.00));
        String token = "testToken";

        when(playerManager.debitWithTransactionId(transaction, token)).thenThrow(new TransactionException("Transaction failed"));

        String requestBody = objectMapper.writeValueAsString(transaction);
        ResponseEntity<Map<String, String>> response = debitTransactionController.debitTransactionWithTransactionId(transaction, token);

        verify(playerManager, times(1)).debitWithTransactionId(transaction, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transaction failed", response.getBody().get("error"));
    }

    @Test
    public void testDebitTransactionWithoutTransactionId_Failure() throws Exception {
        TransactionWithoutId transaction = new TransactionWithoutId(BigDecimal.valueOf(100.00));
        String token = "testToken";

        when(playerManager.debitWithoutTransactionId(transaction, token)).thenThrow(new TransactionException("Transaction failed"));

        String requestBody = objectMapper.writeValueAsString(transaction);
        ResponseEntity<Map<String, String>> response = debitTransactionController.debitTransactionWithoutTransactionId(transaction, token);

        verify(playerManager, times(1)).debitWithoutTransactionId(transaction, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transaction failed", response.getBody().get("error"));
    }
}

