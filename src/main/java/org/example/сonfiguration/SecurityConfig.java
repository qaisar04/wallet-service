package org.example.сonfiguration;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * The {@code SecurityConfig} class is a configuration class for setting up security-related properties, including JWT (JSON Web Token) secret key.
 *
 * <p>This class is annotated with `@Configuration`, indicating that it provides bean definitions.
 *
 * <p>It reads the JWT secret key from an external configuration source (e.g., `application.yml`) using the `@Value` annotation and creates a `Key` bean
 * representing the JWT secret key.
 *
 * <p>Example usage:
 * <pre>
 * // This class is typically used to configure security-related properties in your Spring application.
 * public class MySecurityInitializer {
 *     public static void main(String[] args) {
 *         SpringApplication.run(SecurityConfig.class, args);
 *     }
 * }
 * </pre>
 */
@Configuration
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String jwtSecret;
    
    @Bean
    public Key jwtKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

}
