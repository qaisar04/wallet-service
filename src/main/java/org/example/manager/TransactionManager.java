package org.example.manager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;
import org.example.dto.AuditDto;
import org.example.dto.TransactionDto;
import org.example.logging.aop.annotations.LoggableTime;
import org.example.mapper.AuditMapper;
import org.example.mapper.TransactionMapper;
import org.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.core.domain.types.ActionType.VIEW_TRANSACTION_HISTORY;
import static org.example.core.domain.types.AuditType.*;

@Component
@LoggableTime
@RequiredArgsConstructor
public class TransactionManager {

    private final PlayerManagerImpl playerManager;
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

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = jwtUtils.getPayload(token);
            String username = (String) claims.get("username");

            if(username.equals("admin")) {
                List<Audit> audits = auditService.findAll();

                List<AuditDto> auditsDto = audits.stream()
                        .map(AuditMapper.INSTANCE::toDTO)
                        .collect(Collectors.toList());

                response.put("audits", auditsDto);
                return ResponseEntity.ok(response);
            }
            else {
                response.put("error", "Log in from the admin account");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<HashMap<String, Object>> viewTransactionHistory(String token) {
        HashMap<String, Object> response = new HashMap<>();

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
                playerManager.audit(username, VIEW_TRANSACTION_HISTORY, SUCCESS);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Unauthorized");
                playerManager.audit(username, VIEW_TRANSACTION_HISTORY, FAIL);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        response.put("error", "Unauthorized");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

