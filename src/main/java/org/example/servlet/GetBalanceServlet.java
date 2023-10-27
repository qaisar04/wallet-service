package org.example.servlet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.annotations.Loggable;
import org.example.manager.PlayerManager;

import java.io.IOException;
import java.math.BigDecimal;

@Loggable
@WebServlet("/get-balance")
public class GetBalanceServlet extends HttpServlet {
    PlayerManager playerManager = PlayerManager.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Удалить "Bearer "
            Claims claims = Jwts.parser()
                    .setSigningKey("8U5r&h#KwQ9tj@Lm4ZsFpXv6T")
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            BigDecimal balance = playerManager.getBalance(username);


            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"balance\": " + balance + "}");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
        }
    }
}
