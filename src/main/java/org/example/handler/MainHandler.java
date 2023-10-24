package org.example.handler;


/**
 * The `Main Handler` class is a handler for interacting with the main menu of the
 * application console interface. It provides functionality related to displaying
 * the main menu and handling actions related to it.
 */
public class MainHandler {

    /**
     * Displays the main application menu in the console.
     * The menu includes a list of available actions for the user.
     */
    public void displayMainMenu() {

        String s = """
                | Выберите действие:
                | 1. Регистрация
                | 2. Авторизация
                | 3. Выйти из приложения
                """;

        System.out.println(s);
    }

    /**
     * Completes the execution of the application.
     */
    public void exitApplication() {
        System.out.println("Выход из приложения.");
        System.exit(0);
    }


}
