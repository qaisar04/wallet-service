package org.example.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.annotations.Loggable;
import org.example.manager.PlayerManager;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Api(value = "API for player authorization", description = "this API returns the JWT token in response, thereby ensuring the security of subsequent operations performed")
@RequestMapping(value = "/auth", produces = "application/json")
public class AuthenticatePlayerController {

    private final PlayerManager playerManager;

    @Autowired
    public AuthenticatePlayerController(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Loggable
    @ApiOperation(value = "post method for authenticate player", response = HashMap.class)
    @PostMapping
    public ResponseEntity<?> authenticatePlayer(@RequestBody PlayerWrapper playerWrapper) {
        try {
            return playerManager.authenticatePlayer(playerWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
