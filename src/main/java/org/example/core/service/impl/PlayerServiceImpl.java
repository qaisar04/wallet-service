package org.example.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.Player;
import org.example.core.service.PlayerService;
import org.example.exception.PlayerNotFoundException;
import org.example.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getPlayerBalance(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("The player with id " + id + " not found."))
                .getBalance();
    }

    @Transactional
    @Override
    public void updateBalance(Long id, BigDecimal balance) {
        Player byId = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("The player with id " + id + " not found."));
        byId.setBalance(balance);
        playerRepository.save(byId);
    }

    @Transactional(readOnly = true)
    @Override
    public Player findByUsername(String login) {
        return playerRepository.findByUsername(login)
                .orElseThrow(() -> new PlayerNotFoundException("Player with login " + login + " not found!"));
    }

}
