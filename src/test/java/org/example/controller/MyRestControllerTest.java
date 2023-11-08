package org.example.controller;

import org.example.core.domain.Player;
import org.example.manager.PlayerManagerImpl;
import org.example.manager.TransactionManagerImpl;
import org.example.wrapper.PlayerWrapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MyRestControllerTest {

    @Autowired
    private MyRestController myRestController;

    @MockBean
    private TransactionManagerImpl transactionManager;

    @MockBean
    private PlayerManagerImpl playerManager;

    @Before
    public void setUp() {
        Player player = new Player();
        player.setId(756576);
        player.setUsername("testuser");

        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testuser");
        playerWrapper.setPassword("password");

        when(playerManager.registerPlayer(playerWrapper)).thenReturn(ResponseEntity.ok(player));

        String token = "test-token";
        HashMap<String, Object> transactionHistory = new HashMap<>();
        transactionHistory.put("transactions", "transaction data");
        when(transactionManager.viewTransactionHistory(token)).thenReturn(ResponseEntity.ok(transactionHistory));

        HashMap<String, Object> auditHistory = new HashMap<>();
        auditHistory.put("audits", "audit data");
        when(transactionManager.viewAllAudits(token)).thenReturn(ResponseEntity.ok(auditHistory));

        when(playerManager.getBalance(token)).thenReturn(getBalanceResponse());
    }

    @Test
    public void testRegisterPlayer() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testuser");
        playerWrapper.setPassword("password");

        ResponseEntity<Player> response = myRestController.registerPlayer(playerWrapper);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(756576, response.getBody().getId());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    public void testViewTransactionHistory() {
        String token = "test-token";
        ResponseEntity<HashMap<String, Object>> response = myRestController.viewTransactionHistory(token);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("transaction data", response.getBody().get("transactions"));
    }

    @Test
    public void testViewAuditHistory() {
        String token = "test-token";
        ResponseEntity<HashMap<String, Object>> response = myRestController.viewAuditHistory(token);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("audit data", response.getBody().get("audits"));
    }

    @Test
    public void testGetBalance() {
        String token = "Bearer test-token";

        ResponseEntity<Map<String, String>> response = myRestController.viewPlayerBalance(token);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("1000.0", response.getBody().get("balance"));
    }

    private ResponseEntity<Map<String, String>> getBalanceResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("balance", "1000.0");
        return ResponseEntity.ok(response);
    }
}
