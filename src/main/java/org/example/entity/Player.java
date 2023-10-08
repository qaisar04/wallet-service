package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private String password;
    private double balance;
    private List<Transaction> transactionHistory;

    public Player(String username, String password) {
        this.balance = 0.0;
        this.username = username;
        this.password = password;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
}
