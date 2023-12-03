package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.сonfiguration.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.token.secret}")
    private String SECURE_PASS;

    private final SecurityConfig securityConfig;

    /**
     * Generates a JWT token for a player based on their information.
     *
     * @param player The player for whom the token is generated.
     * @return The generated JWT token as a string.
     */
    public String generateToken(Player player) {

        int ACTIVATION_TIME = 60;
        return Jwts.builder()
                .claim("username", player.getUsername())
                .claim("id", player.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(ACTIVATION_TIME, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(securityConfig.jwtKey().getEncoded()))
                .compact();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getPrivateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, String playerUsername) {
        String username = extractUsername(token);
        return (username.equals(playerUsername))
               && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long extractPlayerId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }


    public <T> T extractClaim(String token, Function<Claims, T> func) {
        Claims claims = getAllClaims(token);
        return func.apply(claims);
    }

    private SecretKey getPrivateKey() {
        byte[] key = Decoders.BASE64.decode(SECURE_PASS);
        return Keys.hmacShaKeyFor(key);
    }

}
