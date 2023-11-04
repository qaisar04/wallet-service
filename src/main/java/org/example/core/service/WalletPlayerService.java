package org.example.core.service;


import org.example.dao.impl.PlayerDaoImpl;
import org.example.core.domain.Player;
import org.example.dto.PlayerDto;
import org.example.manager.PlayerManager;
import org.example.wrapper.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Класс WalletPlayerService представляет собой сервис для управления взаимодействием с игроками.
 * Он предоставляет следующие функциональности:
 * - Регистрация новых игроков.
 * - Аутентификация игроков.
 * - Проверка балансов игроков.
 */
@Service
public class WalletPlayerService implements WalletService<Integer, Player> {

    private PlayerDaoImpl playerDaoImpl;

    @Autowired
    public WalletPlayerService(PlayerDaoImpl playerDaoImpl) {
        this.playerDaoImpl = playerDaoImpl;
    }

    @Override
    public Optional<Player> findById(Integer id) {
        return playerDaoImpl.findById(id);
    }

    public Optional<Player> findByUsername(String username) {
        return playerDaoImpl.findByUsername(username);
    }

    @Override
    public List<Player> findAll() {
        return playerDaoImpl.findAll();
    }

    @Override
    public void save(Player player) {
         playerDaoImpl.save(player);
    }

    @Override
    public void update(Player player) {
        playerDaoImpl.update(player);
    }

    @Override
    public boolean delete(Integer id) {
        return playerDaoImpl.delete(id);
    }

    @Override
    public boolean deleteAll() {
        return playerDaoImpl.deleteAll();
    }

    private WalletPlayerService() {}
}
