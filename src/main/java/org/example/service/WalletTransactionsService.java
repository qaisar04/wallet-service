package org.example.service;

import org.example.types.ActionType;
import org.example.types.AuditType;
import org.example.entity.Player;
import org.example.entity.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Класс WalletTransactionsService представляет собой сервис для управления транзакциями.
 * Он позволяет выполнять дебетовые и кредитные транзакции,  а также просматривать историю транзакций.
 */
public class WalletTransactionsService {

    private WalletPlayerService walletPlayerService;
    private List<String> audits;
    private Map<String, Player> players;

    public WalletTransactionsService(WalletPlayerService walletPlayerService) {
        this.walletPlayerService = walletPlayerService;
        this.audits = walletPlayerService.getAudits();
        this.players = walletPlayerService.getPlayers();
    }


    /**
     * Метод просматривает аудит действий игроков.
     *
     * @return Количество записей в аудите.
     */
    public int viewAllAudits() {
        for (String audit : audits) {
            System.out.println(audit);
        }
        return audits.size();
    }

    /**
     * Метод просматривает историю транзакций для игрока с указанным именем.
     *
     * @param username Имя игрока.
     * @return Количество транзакций в истории.
     */
    public int viewTransactionHistory(String username) {
        Player player = players.get(username);
        if (player != null) {
            List<Transaction> history = player.getTransactionHistory();
            if (history != null) { // Проверка на null перед вызовом isEmpty()
                if (history.isEmpty()) {
                    System.out.println("У пользователя " + username + " нет истории транзакций.");
                    walletPlayerService.audit(username, ActionType.ПРОСМОТР_ИСТОРИИ_ТРАНЗАКЦИЙ, AuditType.FAIL);
                    return 0;
                } else {
                    System.out.println("История транзакций для пользователя " + username + ":");
                    for (Transaction transaction : history) {
                        System.out.println("Идентификатор транзакции: " + transaction.getId() + " | " + transaction.getType() + " " + transaction.getAmount());
                    }
                    walletPlayerService.audit(username, ActionType.ПРОСМОТР_ИСТОРИИ_ТРАНЗАКЦИЙ, AuditType.SUCCESS);
                    return history.size();
                }
            } else {
                System.out.println("История транзакций игрока " + username + " не инициализирована.");
                walletPlayerService.audit(username, ActionType.ПРОСМОТР_ИСТОРИИ_ТРАНЗАКЦИЙ, AuditType.FAIL);
                return -1;
            }
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            walletPlayerService.audit(username, ActionType.ПРОСМОТР_ИСТОРИИ_ТРАНЗАКЦИЙ, AuditType.FAIL);
            return 0;
        }
    }
}
