package org.example;

import org.example.in.WalletConsole;

 /**
 * Главный класс приложения, который запускает консоль для работы с кошельком.
 */
public class Main {
    /**
     * Метод main, который запускает приложение.
     *
     * @throws Exception В случае возникновения исключения при работе с кошельком.
     */
    public static void main(String[] args) throws Exception {
        // Создаем экземпляр консоли кошелька и запускаем приложение.
        WalletConsole walletConsole = new WalletConsole();
        walletConsole.start();
    }
}
