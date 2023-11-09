package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.TransactionException;
import org.example.manager.PlayerManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class DebitTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerManagerImpl playerManager;

    @Test
    public void testDebitTransactionWithTransactionId_Failure() throws Exception {
        TransactionWithId transaction = new TransactionWithId(123, BigDecimal.valueOf(100.00));
        String token = "testToken";

        when(playerManager.debitWithTransactionId(transaction, token)).thenThrow(new TransactionException("Transaction failed"));

        mockMvc.perform(MockMvcRequestBuilders.post("/your-api-endpoint-here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Transaction failed"));
    }

    @Test
    public void testDebitTransactionWithoutTransactionId_Failure() throws Exception {
        TransactionWithoutId transaction = new TransactionWithoutId(BigDecimal.valueOf(100.00));
        String token = "testToken";

        when(playerManager.debitWithoutTransactionId(transaction, token)).thenThrow(new TransactionException("Transaction failed"));

        mockMvc.perform(MockMvcRequestBuilders.post("/your-api-endpoint-here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Transaction failed"));
    }
}
