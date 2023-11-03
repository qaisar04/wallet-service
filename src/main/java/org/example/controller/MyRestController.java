package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@Api(value = "Base functional API", description = "API for player registration, getting transaction history and all audits")
@RequestMapping(value = "/api", produces = "application/json")
    public class MyRestController {

    private final TransactionManager transactionManager;
    private final PlayerManager playerManager;

    @Autowired
    public MyRestController(TransactionManager transactionManager, PlayerManager playerManager) {
        this.transactionManager = transactionManager;
        this.playerManager = playerManager;
    }

    @Loggable
    @ApiOperation(value = "post method for register", response = Player.class)
    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody PlayerWrapper playerWrapper) {
        return playerManager.registerPlayer(playerWrapper);
    }

    @Loggable
    @ApiOperation(value = "post method for viewing all transactions for a certain player, works only after authorization", response = HashMap.class)
    @GetMapping("/history")
    public ResponseEntity<HashMap<String, Object>> viewTransactionHistory(
            @RequestHeader("Authorization") String token) {
        return transactionManager.viewTransactionHistory(token);
    }

    @Loggable
    @ApiOperation(value = "post method for viewing all audits, works only after administrator authorization", response = HashMap.class)
    @GetMapping("/audits")
    public ResponseEntity<HashMap<String, Object>> viewAuditHistory(
            @RequestHeader("Authorization") String token) {
        return transactionManager.viewAllAudits(token);
    }
}

