package org.example.dto;

public record JwtResponse(String login, String accessToken, String refreshToken) {
}
