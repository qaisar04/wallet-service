package org.example.core.service;


import org.example.dao.impl.PlayerDaoImpl;
import org.example.core.domain.Player;

import java.util.*;

/**
 * Класс WalletPlayerService представляет собой сервис для управления взаимодействием с игроками.
 * Он предоставляет следующие функциональности:
 * - Регистрация новых игроков.
 * - Аутентификация игроков.
 * - Проверка балансов игроков.
 */
public class WalletPlayerService implements Service<Integer, Player>{

    private final PlayerDaoImpl playerDaoImpl = PlayerDaoImpl.getInstance();

    private static WalletPlayerService playerService = new WalletPlayerService();

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

    public static WalletPlayerService getInstance(){
        return playerService;
    }


}
