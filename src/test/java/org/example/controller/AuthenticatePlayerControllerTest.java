package org.example.controller;

import org.example.manager.PlayerManagerImpl;
import org.example.wrapper.PlayerWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthenticatePlayerControllerTest {

    private AuthenticatePlayerController authenticatePlayerController;

    @Mock
    private PlayerManagerImpl playerManager;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        authenticatePlayerController = new AuthenticatePlayerController(playerManager);
    }

    @Test
    public void testAuthenticatePlayer_SuccessfulAuthentication() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testUser");
        playerWrapper.setPassword("testPassword");

        ResponseEntity<?> expectedResponse = createSuccessResponse();

        when(playerManager.authenticatePlayer(playerWrapper)).thenAnswer(invocation -> expectedResponse);
        ResponseEntity<?> response = authenticatePlayerController.authenticatePlayer(playerWrapper);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testAuthenticatePlayer_AuthenticationError() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testUser");
        playerWrapper.setPassword("testPassword");

        ResponseEntity<?> expectedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");

        when(playerManager.authenticatePlayer(playerWrapper)).thenThrow(new RuntimeException("Authentication error"));

        ResponseEntity<?> response = authenticatePlayerController.authenticatePlayer(playerWrapper);
        assertEquals(expectedResponse, response);
    }

    private ResponseEntity<?> createSuccessResponse() {
        return ResponseEntity.ok("Authentication is successful");
    }
}