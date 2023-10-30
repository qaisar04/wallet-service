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
import org.example.core.domain.Audit;
import org.example.core.service.WalletAuditService;
import org.example.dto.AuditDto;
import org.example.dto.PlayerDto;
import org.example.mappers.toDtoMapper;
import org.example.util.PropertiesUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/all-audits")
public class ViewAllAuditsServlet extends HttpServlet {

    // для просмотра всех аудитов нужно войти от имени админа и вести token который предоставляется при авторизации
    WalletAuditService walletAuditService = WalletAuditService.getInstance();
    ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
    }

    @Loggable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String username = null;
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Claims claims = Jwts.parser()
                    .setSigningKey(PropertiesUtil.get("secret.key"))
                    .parseClaimsJws(token)
                    .getBody();
            username = claims.getSubject();
        }

        if (!username.equals("admin")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Failed\"}");
            return;
        }

        List<Audit> audits = walletAuditService.findAll();
        List<AuditDto> auditsDto = audits.stream()
                .map(toDtoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());


        if (auditsDto != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(auditsDto);
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
