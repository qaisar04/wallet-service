package kz.baltabayev.audits.manager;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PlayerManager<P, PW, TI, TWI> {

    ResponseEntity<P> registerPlayer(PW playerWrapper);
    ResponseEntity<Map<String, String>> authenticatePlayer(PW playerWrapper);
    ResponseEntity<Map<String, String>> getBalance(String token);
    ResponseEntity<Map<String, String>> creditWithTransactionId(TI transaction, String token);
    ResponseEntity<Map<String, String>> debitWithTransactionId(TI transaction, String token);
    ResponseEntity<Map<String, String>> creditWithoutTransactionId(TWI transactionWithoutId, String token);
    ResponseEntity<Map<String, String>> debitWithoutTransactionId(TWI transactionWithoutId, String token);
}
