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

/**
 * The {@code AuthenticatePlayerController} class is a Spring REST controller responsible for handling player
 * authentication requests. It provides an endpoint for authenticating players and returning a JWT token as a response.
 *
 * <p>This controller is part of the API for player authorization and aims to ensure the security of subsequent
 * operations performed by authenticated players.
 *
 * <p>Example usage:
 * <pre>
 * // Use this controller in your Spring application to handle player authentication.
 * &lt;context:component-scan base-package="org.example.controller" /&gt;
 * </pre>
 *
 * The `authenticatePlayer` method in this controller receives a `PlayerWrapper` object containing player credentials
 * (username and password) in the request body. It invokes the `playerManager` to authenticate the player, and if the
 * authentication is successful, it returns a ResponseEntity containing a JWT token. If an error occurs during the
 * authentication process, an error message is returned with an HTTP 500 status.
 */
@RestController
@Api(value = "API for player authorization", description = "this API returns the JWT token in response, thereby ensuring the security of subsequent operations performed")
@RequestMapping(value = "/auth", produces = "application/json")
public class AuthenticatePlayerController {

    private final PlayerManager playerManager;

    @Autowired
    public AuthenticatePlayerController(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    /**
     * Handles player authentication by receiving player credentials and returning a JWT token in response.
     *
     * @param playerWrapper A {@code PlayerWrapper} object containing player credentials (username and password).
     * @return A ResponseEntity containing a JWT token if authentication is successful, or an error message with an HTTP 500 status if an error occurs.
     */
    @Loggable
    @ApiOperation(value = "post method for authenticate player", response = HashMap.class)
    @PostMapping
    public ResponseEntity<?> authenticatePlayer(@RequestBody PlayerWrapper playerWrapper) {
            return playerManager.authenticatePlayer(playerWrapper);
    }
}
