package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;

import java.io.IOException;

@Loggable
@WebServlet("/register")
public class RegisterPlayerServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private PlayerManager playerManager;

    public RegisterPlayerServlet() {
        this.objectMapper = new ObjectMapper();
        this.playerManager = PlayerManager.getInstance();
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Loggable
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            Player player = objectMapper.readValue(req.getInputStream(), Player.class);
            boolean registrationResult = playerManager.registerPlayer(player.getUsername(), player.getPassword());

            if (registrationResult) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Registration successful\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Registration failed\"}");
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid JSON format\"}");
        }
    }
}
