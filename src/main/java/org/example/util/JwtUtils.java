package org.example.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.example.core.domain.Player;
import org.example.сonfiguration.SecurityConfig;
import org.example.сonfiguration.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

/**
 * The {@code JwtUtils} class provides utility methods for working with JSON Web Tokens (JWT) in the application.
 *
 * <p>It is responsible for generating JWT tokens for player authentication and decoding JWT tokens to extract claims.
 *
 * <p>Example usage:
 * <pre>
 * // Create an instance of JwtUtils in your Spring application.
 * &#64;Autowired
 * private JwtUtils jwtUtils;
 * </pre>
 *
 * The class is annotated with `@Component` for Spring component scanning.
 */
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@Component
public class JwtUtils {

    private SecurityConfig securityConfig;

    @Autowired
    public JwtUtils(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    /**
     * Generates a JWT token for a player based on their information.
     *
     * @param player The player for whom the token is generated.
     * @return The generated JWT token as a string.
     */
    public String generateToken(Player player) {

        return Jwts.builder()
                .claim("id", player.getId())
                .claim("username", player.getUsername())
                .issuer("http://localhost:8080/")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(securityConfig.jwtKey().getEncoded()))
                .compact();

    }

    /**
     * Parses and verifies a JWT token to extract its payload claims.
     *
     * @param token The JWT token to parse.
     * @return The claims extracted from the token's payload.
     * @throws RuntimeException if there is an issue parsing or verifying the token.
     */
    public Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(securityConfig.jwtKey().getEncoded()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
