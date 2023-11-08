package org.example.manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kz.baltabayev.audits.manager.TransactionManager;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.AuditDto;
import org.example.dto.TransactionDto;
import org.example.exception.UnauthorizedAccessException;
import org.example.logging.aop.annotations.LoggableTime;
import org.example.mapper.AuditMapper;
import org.example.mapper.TransactionMapper;
import org.example.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
@LoggableTime
@RequiredArgsConstructor
public class TransactionManagerImpl implements TransactionManager<String,ResponseEntity<HashMap<String, Object>>> {

    private final WalletPlayerService playerService;
    private final WalletTransactionsService transactionsService;
    private final WalletAuditService auditService;
    private final JwtUtils jwtUtils;


    /**
     * The method looks at the audit of the players' actions.
     *
     * @return The number of records in the audit.
     */
    public ResponseEntity<HashMap<String, Object>> viewAllAudits(String token) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Claims claims = jwtUtils.getPayload(token);
                String username = (String) claims.get("username");

                if ("admin".equals(username)) {
                    List<Audit> audits = auditService.findAll();

                    if (audits.isEmpty()) {
                        response.put("message", "No audits found.");
                        return ResponseEntity.ok(response);
                    }

                    List<AuditDto> auditsDto = audits.stream()
                            .map(AuditMapper.INSTANCE::toDTO)
                            .collect(Collectors.toList());

                    response.put("audits", auditsDto);
                    return ResponseEntity.ok(response);
                } else {
                    throw new UnauthorizedAccessException("Access denied. Log in with admin account.");
                }
            } else {
                throw new UnauthorizedAccessException("Unauthorized access");
            }
        } catch (UnauthorizedAccessException accessException) {
            response.put("error", "Unauthorized. Access denied. Log in with admin account.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<HashMap<String, Object>> viewTransactionHistory(String token) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Claims claims = jwtUtils.getPayload(token);
                String username = (String) claims.get("username");

                Optional<Player> optionalPlayer = playerService.findByUsername(username);

                if (optionalPlayer.isPresent()) {
                    Player player = optionalPlayer.get();
                    List<Transaction> transactionsList = transactionsService.findByPlayerId(player.getId());

                    List<TransactionDto> transactionsDto = transactionsList.stream()
                            .map(TransactionMapper.INSTANCE::toDTO)
                            .collect(Collectors.toList());

                    response.put("username", username);
                    response.put("transactions", transactionsDto);
                    return ResponseEntity.ok(response);
                } else {
                    throw new UnauthorizedAccessException("Unauthorized access");
                }
            } else {
                throw new UnauthorizedAccessException("Unauthorized access");
            }
        } catch (UnauthorizedAccessException exception) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

