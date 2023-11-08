package org.example.controller;

import org.example.controller.CreditTransactionController;
import org.example.core.domain.Player;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.manager.PlayerManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreditTransactionControllerTest {

    @Mock
    private PlayerManagerImpl playerManager;

    @InjectMocks
    private CreditTransactionController controller;

    @Mock
    private WalletPlayerService playerService;

    @Mock
    private WalletTransactionsService transactionsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreditTransactionWithTransactionId() {
        TransactionWithId transaction = new TransactionWithId(1, BigDecimal.valueOf(100.0));
        String token = "Bearer yourAuthTokenHere";

        when(playerService.findByUsername(anyString()))
                .thenReturn(Optional.of(new Player(/* create a Player object here */)));
        when(transactionsService.findById(1))
                .thenReturn(Optional.empty());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Транзакция успешно завершена");
        when(playerManager.creditWithTransactionId(transaction, token))
                .thenReturn(ResponseEntity.ok(response));

        ResponseEntity<Map<String, String>> result = controller.creditTransactionWithTransactionId(transaction, token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Транзакция успешно завершена", result.getBody().get("message"));
    }

    @Test
    public void testCreditTransactionWithoutTransactionId() {
        TransactionWithoutId transactionWithoutId = new TransactionWithoutId(BigDecimal.valueOf(50.0));
        String token = "Bearer yourAuthTokenHere";

        when(playerManager.creditWithoutTransactionId(any(), anyString()))
                .thenCallRealMethod();

        ResponseEntity<Map<String, String>> result = controller.creditTransactionWithoutTransactionId(transactionWithoutId, token);

        assertNotNull(result);
    }
}
