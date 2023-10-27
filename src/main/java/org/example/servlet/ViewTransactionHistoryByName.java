package org.example.servlet;

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
import org.example.core.service.WalletTransactionsService;
import org.example.dto.PlayerDto;
import org.example.dto.TransactionDto;
import org.example.manager.TransactionManager;
import org.example.mappers.toDtoMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/transactions")
public class ViewTransactionHistoryByName extends HttpServlet {

    private final TransactionManager transactionManager = TransactionManager.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

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


        List<Transaction> transactions = transactionManager.viewTransactionHistory(username);
        List<TransactionDto> transactionsDto = transactions.stream()
                .map(toDtoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();

        if (transactionsDto != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(transactionsDto);
            try (var printWriter = resp.getWriter()) {
                printWriter.write(json);
            } catch (IOException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            String errorMessage = "No players found";
            try (var printWriter = resp.getWriter()) {
                printWriter.write("{\"error\": \"" + errorMessage + "\"}");
            } catch (IOException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
