package org.example.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.core.domain.Player;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.dto.PlayerDto;
import org.example.manager.PlayerManager;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/register")
public class RegisterPlayerServlet extends HttpServlet {

    WalletPlayerService playerService = WalletPlayerService.getInstance();
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Десериализация JSON в объект player
            Player player = objectMapper.readValue(req.getInputStream(), Player.class);
            boolean registrationResult = playerManager.registerPlayer(player.getUsername(), player.getPassword());

            if (registrationResult) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Registration successful\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Registration failed\"}");
            }
        } catch (JsonParseException | JsonMappingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid JSON format\"}");
        }
    }
}

