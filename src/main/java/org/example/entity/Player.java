package org.example.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Player, который описывает пользователя
 */
@Data
public class Player {
    private String username;
    private String password;
    private BigDecimal balance;
    private List<Transaction> transactionHistory;

    /**
     * Конструктор для создания обьекта класса Player
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     */
    public Player(String username, String password) {
        this.balance = BigDecimal.valueOf(0.0);
        this.username = username;
        this.password = password;
        this.transactionHistory = new ArrayList<>();
    }


}
