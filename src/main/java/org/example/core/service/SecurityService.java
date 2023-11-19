package org.example.core.service;

import org.example.core.domain.Player;
import org.example.dto.JwtResponse;
import org.springframework.stereotype.Service;

public interface SecurityService {

    Player register(String username, String password);

    String authorization(String login, String password);
}
