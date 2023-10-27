package org.example;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.in.WalletConsole;
import org.example.liquibase.LiquibaseDemo;

/**
 * The main class of the application that runs the console to work with the wallet.
 */
public class Main {

    private static Main main = new Main();

    /**
     * The main method that launches the application.
     */
    public static void main(String[] args) {
        MainHandler mainHandler = new MainHandler();
        UserHandler userHandler = new UserHandler();
        AdminHandler adminHandler = new AdminHandler();

//        LiquibaseDemo liquibaseDemo = LiquibaseDemo.getInstance();
//        liquibaseDemo.runMigrations();

        WalletConsole walletConsole = new WalletConsole();
        walletConsole.start(mainHandler, userHandler, adminHandler);

    }

    public static Main getInstance() {
        return main;
    }
}
