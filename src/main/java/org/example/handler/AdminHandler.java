package org.example.handler;

/**
 * The `AdminHandler` class is a handler for administrator interaction with
 * the application console interface. It provides functionality related to menu display.
 */
public class AdminHandler {
    /**
     * Displays the menu for the administrator in the console.
     * The menu includes a list of available actions for the administrator.
     */
    public void displayAdminMenu() {
        String s = """
                | Привет, Админ. Рад видеть вас!
                | Выберите действие:
                | 1. Просмотр аудитов
                | 2. Выйти из аккаунта
                | 3. Выйти из приложения
                """;

        System.out.println(s);
    }
}
