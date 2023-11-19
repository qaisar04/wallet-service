package org.example.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.core.domain.types.PlayerRole;
import org.example.core.service.SecurityService;
import org.example.exception.AuthorizeException;
import org.example.exception.RegisterException;
import org.example.repository.PlayerRepository;
import org.example.security.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final JwtProvider jwtProvider;
    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public Player register(String username, String password) {
        Optional<Player> player = playerRepository.findByUsername(username);
        if(player.isPresent()) {
            throw new RegisterException("The player with this login already exists.");
        }

        Player newPlayer = Player.builder()
                .username(username)
                .password(password)
                .balance(BigDecimal.ZERO)
                .playerRole(PlayerRole.USER)
                .build();

        return playerRepository.save(newPlayer);
    }

    @Override
    @Transactional
    public String authorization(String username, String password) {
        Optional<Player> optionalPlayer = playerRepository.findByUsername(username);
        if(optionalPlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }
        Player player = optionalPlayer.get();

        if (player.getPassword().equals(password)) {
            return jwtProvider.generateToken(optionalPlayer.get());
        } else {
            throw new AuthorizeException("Invalid username or password.");
        }
    }
}
