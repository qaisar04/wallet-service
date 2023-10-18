package org.example.serviceTest;

import org.example.core.domain.Player;
import org.example.dao.impl.PlayerDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerDaoImplTest {
    private PlayerDaoImpl playerDao;
    private Player testPlayer;

    // я не успел сделать тест контейнеры

    @BeforeEach
    public void setUp() {
        playerDao = PlayerDaoImpl.getInstance();
        playerDao.deleteAll();
        testPlayer = createSamplePlayer("TestPlayer", "password", BigDecimal.valueOf(100.0));
        playerDao.save(testPlayer);
    }

    @Test
    public void testFindById() {
        Optional<Player> foundPlayer = playerDao.findById(testPlayer.getId());
        assertTrue(foundPlayer.isPresent());
        assertEquals(testPlayer.getId(), foundPlayer.get().getId());
        assertEquals(testPlayer.getFullName(), foundPlayer.get().getFullName());

        Optional<Player> notFoundPlayer = playerDao.findById(999);
        assertFalse(notFoundPlayer.isPresent());
    }

    @Test
    public void testFindByUsername() {
        Optional<Player> foundPlayer = playerDao.findByUsername(testPlayer.getFullName());
        assertTrue(foundPlayer.isPresent());
        assertEquals(testPlayer.getId(), foundPlayer.get().getId());
        assertEquals(testPlayer.getFullName(), foundPlayer.get().getFullName());

        Optional<Player> notFoundPlayer = playerDao.findByUsername("NonExistentUsername");
        assertFalse(notFoundPlayer.isPresent());
    }

    @Test
    public void testFindAll() {
        List<Player> players = playerDao.findAll();
        assertFalse(players.isEmpty());
    }

    @Test
    public void testUpdate() {
        testPlayer.setBalance(BigDecimal.valueOf(200.0));
        playerDao.update(testPlayer);

        Optional<Player> updatedPlayer = playerDao.findById(testPlayer.getId());
        assertTrue(updatedPlayer.isPresent());

        BigDecimal expectedBalance = BigDecimal.valueOf(200.0);
        BigDecimal actualBalance = updatedPlayer.get().getBalance();

        assertEquals(expectedBalance.setScale(2, RoundingMode.HALF_UP), actualBalance.setScale(2, RoundingMode.HALF_UP));
    }


    @Test
    public void testDelete() {
        assertTrue(playerDao.delete(testPlayer.getId()));

        Optional<Player> deletedPlayer = playerDao.findById(testPlayer.getId());
        assertFalse(deletedPlayer.isPresent());
    }

    @Test
    public void testDeleteAll() {
        boolean deleted = playerDao.deleteAll();
        assertTrue(deleted);

        List<Player> players = playerDao.findAll();
        assertTrue(players.isEmpty());
    }

    private Player createSamplePlayer(String username, String password, BigDecimal balance) {
        return Player.builder()
                .fullName(username)
                .password(password)
                .balance(balance)
                .build();
    }
}
