package org.example.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;
import org.example.util.PropertiesUtil;

import java.io.IOException;
import java.util.Date;

@WebServlet("/authenticate")
public class AuthenticatePlayerServlet extends HttpServlet {

    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    @Loggable
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Player player = objectMapper.readValue(req.getInputStream(), Player.class);
            boolean authenticateResult = playerManager.authenticatePlayer(player.getUsername(), player.getPassword());

            if (authenticateResult) {
                Date expirationDate = new Date(System.currentTimeMillis() + 3600 * 1000);

                String jwt = Jwts.builder()
                        .setSubject(player.getUsername())
                        .setExpiration(expirationDate)
                        .signWith(SignatureAlgorithm.HS256, PropertiesUtil.get("secret.key"))
                        .compact();

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Authentication is successful\", \"jwt\": \"" + jwt + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Authentication is failed\"}");
            }
        } catch (JsonParseException | JsonMappingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid JSON format\"}");
        }
    }
}
