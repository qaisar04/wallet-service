package org.example.controller;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.core.service.WalletPlayerService;
import org.example.dto.transaction.TransactionWithId;
import org.example.dto.transaction.TransactionWithoutId;
import org.example.exception.TransactionException;
import org.example.manager.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Api(value = "API for debit transaction", description = "this API allows you to make a debit transaction")
@RequestMapping(value = "/api/debit", produces = "application/json")
public class DebitTransactionController {

    private final PlayerManager playerManager;

    @Autowired
    public DebitTransactionController(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Loggable
    @ApiOperation(value = "post method for debet with transaction id", response = HashMap.class)
    @PostMapping("/id")
    public ResponseEntity<Map<String, String>> debitTransactionWithTransactionId(
            @RequestBody TransactionWithId transaction,
            @RequestHeader("Authorization") String token) {
        try {
            return playerManager.debitWithTransactionId(transaction, token);
        } catch (TransactionException e) {
            Map<String, String> debitWithTransactionIdResponse = new HashMap<>();
            debitWithTransactionIdResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debitWithTransactionIdResponse);
        }
    }

    @Loggable
    @ApiOperation(value = "post method for debet without transaction id", response = HashMap.class)
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> debitTransactionWithoutTransactionId(
            @RequestBody TransactionWithoutId transaction,
            @RequestHeader("Authorization") String token) {
        try {
            return playerManager.debitWithoutTransactionId(transaction, token);
        } catch (TransactionException e) {
            Map<String, String> debitWithTransactionIdResponse = new HashMap<>();
            debitWithTransactionIdResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(debitWithTransactionIdResponse);
        }
    }
}


