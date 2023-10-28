package org.example.servlet;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.service.WalletPlayerService;
import org.example.dto.TransactionDto;
import org.example.manager.PlayerManager;

import java.io.IOException;
import java.util.Optional;

@Loggable
@WebServlet("/debit-without-id")
public class DebitWithoutTransactionIdServlet extends HttpServlet {

    private final PlayerManager playerManager = PlayerManager.getInstance();
    private final WalletPlayerService walletPlayerService = WalletPlayerService.getInstance();

    @Loggable
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String username = null;
            String token = req.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Claims claims = Jwts.parser()
                        .setSigningKey("8U5r&h#KwQ9tj@Lm4ZsFpXv6T")
                        .parseClaimsJws(token)
                        .getBody();
                username = claims.getSubject();
            }

            TransactionDto transaction = objectMapper.readValue(req.getInputStream(), TransactionDto.class);
            Optional<Player> player = walletPlayerService.findByUsername(username);

            boolean result = false;
            if (player.isPresent() && player.get().getUsername().equals(username)) {
                result = playerManager.debitWithoutTransactionId(
                        player.get().getUsername(),
                        transaction.getAmount()
                );
            }

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Debit transaction is successful\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Debit transaction failed\"}");
            }
        } catch (JsonParseException | JsonMappingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid JSON format\"}");
        }
    }
}
