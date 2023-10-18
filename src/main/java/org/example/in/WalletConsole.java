package org.example.in;

import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.manager.PlayerManager;
import org.example.manager.TransactionManager;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Wallet Console class represents a text interface for interacting with the wallet service.
 * The user can perform registration, authentication, view the balance, debit and credit,
 * as well as view the transaction history.
 */
public class WalletConsole {

    PlayerManager playerManager;
    TransactionManager transactionManager;
    private static WalletConsole walletConsole = new WalletConsole();

    public static WalletConsole getInstance() {
        return walletConsole;
    }

    public WalletConsole() {
        playerManager = new PlayerManager();
        transactionManager = new TransactionManager(playerManager);
    }


    static String loggedInUsername = null; // username того, кто вошел
    static boolean loggedIn = false; // для отслеживания авторизации
    Scanner scanner = new Scanner(System.in);

    /**
     * The start method launches a text interface for interacting with the wallet.
     */
    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler) {
        while (true) {
            if (!loggedIn) {
                mainHandler.displayMainMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1 -> {
                        System.out.print("Введите имя пользователя: ");
                        String username = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        if (!playerManager.registerPlayer(username, password)) {
                            continue;
                        }
                    }
                    case 2 -> {
                        System.out.print("Введите имя пользователя: ");
                        String authenticateUsername = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String authenticatePassword = scanner.nextLine();
                        if (playerManager.authenticatePlayer(authenticateUsername, authenticatePassword)) {
                            loggedIn = true;
                            loggedInUsername = authenticateUsername;
                        }
                    }
                    case 3 -> {
                        mainHandler.exitApplication();
                    }
                    default -> {
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                    }
                }
            } else if ("admin".equals(loggedInUsername)) {
                adminHandler.displayAdminMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1 -> {
                        transactionManager.viewAllAudits();
                    }
                    case 2 -> {
                        userHandler.logout();
                    }
                    case 3 -> {
                        mainHandler.exitApplication();
                    }
                    default -> {
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                    }
                }
            } else if (loggedInUsername != null) {
                userHandler.displayUserMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1 -> {
                        BigDecimal balance = playerManager.getBalance(loggedInUsername);
                        System.out.println("Баланс игрока " + loggedInUsername + ": " + balance);
                    }
                    case 2 -> {
                        userHandler.displayUserCreditAndDebet();
                        int choiceDebetTransactionId = userHandler.readChoice();
                        switch (choiceDebetTransactionId) {
                            case 1 -> {
                                System.out.print("Введите сумму дебета: ");
                                BigDecimal debetAmountTransactionId = BigDecimal.ZERO;
                                try {
                                    debetAmountTransactionId = scanner.nextBigDecimal();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода суммы кредита. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }

                                int transactionIdForDebet = 0;
                                System.out.print("Введите идентификатор транзакции: ");
                                try {
                                    transactionIdForDebet = scanner.nextInt();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода идентификатора транзакции. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }
                                playerManager.debitWithTransactionId(loggedInUsername, transactionIdForDebet, debetAmountTransactionId);
                            }
                            case 2 -> {
                                System.out.print("Введите сумму дебета: ");
                                BigDecimal debetAmount = BigDecimal.ZERO;
                                try {
                                    debetAmount = scanner.nextBigDecimal();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода суммы дебета. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }
                                playerManager.debitWithoutTransactionId(loggedInUsername, debetAmount);
                            }
                        }
                    }
                    case 3 -> {
                        userHandler.displayUserCreditAndDebet();
                        int choiceCreditTransactionId = userHandler.readChoice();
                        switch (choiceCreditTransactionId) {
                            case 1 -> {
                                System.out.print("Введите сумму кредита: ");
                                BigDecimal creditAmountTransactionId = BigDecimal.ZERO;
                                try {
                                    creditAmountTransactionId = scanner.nextBigDecimal();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода суммы кредита. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }

                                int transactionIdForCredit = 0;
                                System.out.print("Введите идентификатор транзакции: ");
                                try {
                                    transactionIdForCredit = scanner.nextInt();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода идентификатора транзакции. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }
                                playerManager.creditWithTransactionId(loggedInUsername, transactionIdForCredit, creditAmountTransactionId);
                            }
                            case 2 -> {
                                System.out.print("Введите сумму кредита: ");
                                BigDecimal creditAmount = BigDecimal.ZERO;
                                try {
                                    creditAmount = scanner.nextBigDecimal();
                                } catch (InputMismatchException e) {
                                    System.err.println("Ошибка ввода суммы кредита. Введено недопустимое значение.");
                                    scanner.nextLine();
                                    continue;
                                }
                                playerManager.creditWithoutTransactionId(loggedInUsername, creditAmount);
                            }
                        }
                    }
                    case 4 -> {
                        transactionManager.viewTransactionHistory(loggedInUsername);
                    }
                    case 5 -> {
                        userHandler.logout();
                    }
                    case 6 -> {
                        mainHandler.exitApplication();
                    }
                    default -> {
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                    }
                }
            } else {
                System.out.println("Вы не авторизованы. Войдите сначала в аккаунт.");
            }
        }
    }

    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
