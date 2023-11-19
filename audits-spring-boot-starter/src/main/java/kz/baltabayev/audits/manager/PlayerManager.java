package kz.baltabayev.audits.manager;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PlayerManager<P, TI, TWI> {

    ResponseEntity<P> registerPlayer(String username, String password);
    ResponseEntity<Map<String, String>> authenticatePlayer(String username, String password);
    ResponseEntity<Map<String, String>> getBalance(String token);
    ResponseEntity<Map<String, String>> creditWithTransactionId(TI transaction, String token);
    ResponseEntity<Map<String, String>> debitWithTransactionId(TI transaction, String token);
    ResponseEntity<Map<String, String>> creditWithoutTransactionId(TWI transactionWithoutId, String token);
    ResponseEntity<Map<String, String>> debitWithoutTransactionId(TWI transactionWithoutId, String token);
}
