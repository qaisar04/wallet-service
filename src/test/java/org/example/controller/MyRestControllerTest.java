package org.example.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;
import org.example.wrapper.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class MyRestControllerTest {

    @Mock
    private TransactionManager transactionManager;

    @Mock
    private PlayerManager playerManager;

    @InjectMocks
    private MyRestController myRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterPlayer() {
        Player player = new Player();
        player.setId(756576);
        player.setUsername("testuser");

        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testuser");
        playerWrapper.setPassword("password");

        Mockito.when(playerManager.registerPlayer(playerWrapper)).thenReturn(ResponseEntity.ok(player));

        ResponseEntity<Player> response = myRestController.registerPlayer(playerWrapper);

        assert response.getStatusCodeValue() == 200;
        assert response.getBody() != null;
        assert response.getBody().getId() == 756576;
        assert response.getBody().getUsername().equals("testuser");
    }

    @Test
    public void testViewTransactionHistory() {
        String token = "test-token";
        HashMap<String, Object> transactionHistory = new HashMap<>();
        transactionHistory.put("transactions", "transaction data");

        Mockito.when(transactionManager.viewTransactionHistory(token)).thenReturn(ResponseEntity.ok(transactionHistory));

        ResponseEntity<HashMap<String, Object>> response = myRestController.viewTransactionHistory(token);

        assert response.getStatusCodeValue() == 200;
        assert response.getBody() != null;
        assert response.getBody().get("transactions").equals("transaction data");
    }

    @Test
    public void testViewAuditHistory() {
        String token = "test-token";
        HashMap<String, Object> auditHistory = new HashMap<>();
        auditHistory.put("audits", "audit data");

        Mockito.when(transactionManager.viewAllAudits(token)).thenReturn(ResponseEntity.ok(auditHistory));

        ResponseEntity<HashMap<String, Object>> response = myRestController.viewAuditHistory(token);

        assert response.getStatusCodeValue() == 200;
        assert response.getBody() != null;
        assert response.getBody().get("audits").equals("audit data");
    }

    @Test
    public void testGetBalance() {
        String token = "Bearer test-token";

        Mockito.when(playerManager.getBalance(token)).thenReturn(getBalanceResponse());

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

