package org.example.controller;

import org.example.manager.PlayerManagerImpl;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticatePlayerControllerTest {

    @MockBean
    private PlayerManagerImpl playerManager;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testUser");
        playerWrapper.setPassword("testPassword");

        ResponseEntity<Map<String, String>> expectedResponse = createSuccessResponse();
        when(playerManager.authenticatePlayer(playerWrapper)).thenReturn(expectedResponse);
    }

    @Test
    public void testAuthenticatePlayer_SuccessfulAuthentication() throws Exception {
        mockMvc.perform(post("/auth")
                        .contentType("application/json")
                        .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticatePlayer_AuthenticationError() throws Exception {
        PlayerWrapper playerWrapper = new PlayerWrapper();
        playerWrapper.setUsername("testUser");
        playerWrapper.setPassword("testPassword");

        ResponseEntity<Map<String, String>> expectedErrorResponse = createErrorResponse();
        when(playerManager.authenticatePlayer(playerWrapper)).thenReturn(expectedErrorResponse);

        mockMvc.perform(post("/auth")
                        .contentType("application/json")
                        .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}")
                )
                .andExpect(status().isInternalServerError());
    }

    private ResponseEntity<Map<String, String>> createSuccessResponse() {
        Map<String, String> responseMap = Map.of("message", "Authentication is successful", "jwt", "yourJwtToken");
        return ResponseEntity.ok(responseMap);
    }

    private ResponseEntity<Map<String, String>> createErrorResponse() {
        Map<String, String> errorMap = Map.of("error", "Authentication is failed");
        return ResponseEntity.badRequest().body(errorMap);
    }
}
