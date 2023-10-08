package org.example.in;

import org.example.service.WalletService;

import java.util.Scanner;

/**
 * Класс WalletConsole представляет текстовый интерфейс для взаимодействия с сервисом кошелька.
 * Пользователь может выполнять регистрацию, аутентификацию, просматривать баланс,
 * осуществлять дебет и кредит, а также просматривать историю транзакций.
 */
public class WalletConsole {

    Scanner scanner = new Scanner(System.in);
    WalletService walletService = new WalletService();
    String loggedInUsername = null; // username того, кто вошел
    boolean loggedIn = false; // для отслеживания авторизации

    /**
     * Метод start запускает текстовый интерфейс для взаимодействия с кошельком.
     *
     * @throws Exception в случае возникновения исключений
     */
    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!loggedIn) {
                System.out.println("_______________________________");
                System.out.println("Выберите действие:");
                System.out.println("1. Регистрация");
                System.out.println("2. Авторизация");
                System.out.println("_______________________________");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Введите имя пользователя: ");
                        String username = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        walletService.registerPlayer(username, password);
                        break;
                    case 2:
                        System.out.print("Введите имя пользователя: ");
                        String authUsername = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String authPassword = scanner.nextLine();
                        if (walletService.authenticatePlayer(authUsername, authPassword)) {
                            loggedIn = true;
                            loggedInUsername = authUsername;
                        }
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }

            } else if ("admin".equals(loggedInUsername)) {
                System.out.println("_______________________________");
                System.out.println("Привет, Админ. Рад видеть вас!");
                System.out.println("Выберите действие:");
                System.out.println("1. Просмотр аудитов");
                System.out.println("2. Выйти из аккаунта");
                System.out.println("3. Выйти из приложения");
                System.out.println("_______________________________");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        walletService.viewAllAudits();
                        break;
                    case 2:
                        loggedIn = false;
                        loggedInUsername = null;
                        System.out.println("Выход из аккаунта.");
                        break;
                    case 3:
                        System.out.println("Выход из приложения.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if (loggedInUsername != null) {
                System.out.println("_______________________________");
                System.out.println("Выберите действие:");
                System.out.println("1. Просмотр баланса");
                System.out.println("2. Дебет");
                System.out.println("3. Кредит");
                System.out.println("4. История транзакций");
                System.out.println("5. Выйти из аккаунта");
                System.out.println("6. Выйти из приложения");
                System.out.println("_______________________________");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        double balance = walletService.getBalance(loggedInUsername);
                        System.out.println("Баланс игрока " + loggedInUsername + ": " + balance);
                        break;
                    case 2:
                        System.out.print("Введите сумму дебета: ");
                        double debitAmount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Введите идентификатор транзакции: ");
                        String debitTransactionId = scanner.nextLine();
                        walletService.debit(loggedInUsername, debitTransactionId, debitAmount);
                        break;
                    case 3:
                        System.out.print("Введите сумму кредита: ");
                        double creditAmount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Введите идентификатор транзакции: ");
                        String creditTransactionId = scanner.nextLine();
                        walletService.credit(loggedInUsername, creditTransactionId, creditAmount);
                        break;

                    case 4:
                        walletService.viewTransactionHistory(loggedInUsername);
                        break;
                    case 5:
                        loggedIn = false;
                        loggedInUsername = null;
                        System.out.println("Выход из аккаунта.");
                        break;
                    case 6:
                        System.out.println("Выход из приложения.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else {
                System.out.println("Вы не авторизованы. Войдите сначала в аккаунт.");
            }
        }
    }
}
