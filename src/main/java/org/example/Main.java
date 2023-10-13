package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.in.WalletConsole;

/**
 * Главный класс приложения, который запускает консоль для работы с кошельком.
 */
public class Main {
    /**
     * Метод main, который запускает приложение.
     */
    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler = new AdminHandler();

        WalletConsole walletConsole = new WalletConsole();
        walletConsole.start(mainHandler, userHandler, adminHandler);
    }
}
