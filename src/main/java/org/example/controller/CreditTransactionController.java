package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotations.Loggable;
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


@RestController
@Api(value = "API for credit transaction", description = "this API allows you to make a credit transaction")
@RequestMapping(value = "/api/credit", produces = "application/json")
public class CreditTransactionController {

    private final PlayerManager playerManager;

    @Autowired
    public CreditTransactionController(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Loggable
    @ApiOperation(value = "post method for credit with transaction id", response = HashMap.class)
    @PostMapping("/id")
    public ResponseEntity<Map<String, String>> creditTransactionWithTransactionId(
            @RequestBody TransactionWithId transaction,
            @RequestHeader("Authorization") String token) {
        try {
            return playerManager.creditWithTransactionId(transaction, token);
        } catch (TransactionException e) {
            Map<String, String> creditWithTransactionIdResponse = new HashMap<>();
            creditWithTransactionIdResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(creditWithTransactionIdResponse);
        }
    }

    @Loggable
    @ApiOperation(value = "post method for credit without transaction id", response = HashMap.class)
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> creditTransactionWithoutTransactionId(
            @RequestBody TransactionWithoutId transaction,
            @RequestHeader("Authorization") String token) {
        try {
            return playerManager.creditWithoutTransactionId(transaction, token);
        } catch (Exception e) {
            Map<String, String> creditWithoutTransactionIdResponse = new HashMap<>();
            creditWithoutTransactionIdResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(creditWithoutTransactionIdResponse);
        }
    }
}
