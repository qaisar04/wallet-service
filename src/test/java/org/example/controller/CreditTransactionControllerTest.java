package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.CreditTransactionController;
import org.example.manager.PlayerManager;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.TransactionException;
import org.example.wrapper.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreditTransactionControllerTest {

    private CreditTransactionController creditTransactionController;
    private PlayerManager playerManager;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        playerManager = mock(PlayerManager.class);
        objectMapper = new ObjectMapper();
        creditTransactionController = new CreditTransactionController(playerManager);
    }

    @Test
    public void testCreditTransactionWithTransactionId_Success() throws Exception {
        TransactionWithId transaction = new TransactionWithId(123, BigDecimal.valueOf(100.00));
        String token = "testToken";

        ResponseEntity<Map<String, String>> expectedResponse = ResponseEntity.ok(Map.of("message", "Credit is successful"));
        when(playerManager.creditWithTransactionId(transaction, token)).thenReturn(expectedResponse);

        String requestBody = objectMapper.writeValueAsString(transaction);
        ResponseEntity<Map<String, String>> response = creditTransactionController.creditTransactionWithTransactionId(transaction, token);

        verify(playerManager, times(1)).creditWithTransactionId(transaction, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }

    @Test
    public void testCreditTransactionWithTransactionId_Failure() throws Exception {
        TransactionWithId transaction = new TransactionWithId(123, BigDecimal.valueOf(100.00));
        String token = "testToken";

        when(playerManager.creditWithTransactionId(transaction, token)).thenThrow(new TransactionException("Transaction failed"));

        String requestBody = objectMapper.writeValueAsString(transaction);
        ResponseEntity<Map<String, String>> response = creditTransactionController.creditTransactionWithTransactionId(transaction, token);

        verify(playerManager, times(1)).creditWithTransactionId(transaction, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transaction failed", response.getBody().get("error"));
    }

    @Test
    public void testCreditTransactionWithoutTransactionId_Success() throws Exception {
        TransactionWithoutId transaction = new TransactionWithoutId(BigDecimal.valueOf(100.00));
        String token = "testToken";

        ResponseEntity<Map<String, String>> expectedResponse = ResponseEntity.ok(Map.of("message", "Credit is successful"));
        when(playerManager.creditWithoutTransactionId(transaction, token)).thenReturn(expectedResponse);

        String requestBody = objectMapper.writeValueAsString(transaction);
        ResponseEntity<Map<String, String>> response = creditTransactionController.creditTransactionWithoutTransactionId(transaction, token);

        verify(playerManager, times(1)).creditWithoutTransactionId(transaction, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }

}
