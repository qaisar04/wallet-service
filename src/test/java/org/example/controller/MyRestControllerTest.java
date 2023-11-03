package org.example.controller;

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
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

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
}

