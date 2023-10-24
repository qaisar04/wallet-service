package org.example.handler;

import org.example.in.WalletConsole;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The `UserHandler` class serves as a handler for user interaction with the application's console interface.
 * It provides functionality related to displaying user menus, reading input, and managing user actions.
 */
public class UserHandler {

    Scanner scanner = new Scanner(System.in);

    /**
     * The wallet console instance for managing user sessions and actions.
     */
    WalletConsole walletConsole = new WalletConsole();

    /**
     * Reads the user's choice from the console.
     *
     * @return The user's choice.
     */
    public int readChoice() {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
        }
        return choice;
    }

    /**
     * Logs out the user and resets associated data.
     */
    public void logout() {
        walletConsole.setLoggedInUsername(null);
        walletConsole.setLoggedIn(false);
        System.out.println("Выход из аккаунта.");
    }

    /**
     * Displays the user menu in the console.
     * The menu includes a list of available user actions.
     */
    public void displayUserMenu() {

        String s = """
                | Выберите действие:
                | 1. Просмотр баланса
                | 2. Дебет
                | 3. Кредит
                | 4. История транзакций
                | 5. Выйти из аккаунта
                | 6. Выйти из приложения
                """;

        System.out.println(s);
    }

    /**
     * Displays the user menu related to credit and debit transactions.
     * The menu includes the user's choice for providing a transaction identifier.
     */
    public void displayUserCreditAndDebet() {

        String s = """
                | Желаете ли вы ввести идентификатор для транзакции?
                | 1. Да
                | 2. Нет
                """;

        System.out.println(s);
    }


}
