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

@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@Component
public class JwtUtils {

    private SecurityConfig securityConfig;

    @Autowired
    public JwtUtils(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

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
