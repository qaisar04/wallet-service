package org.example.manager;

import org.example.core.domain.Audit;
import org.example.core.domain.Player;
import org.example.core.domain.Transaction;
import org.example.core.service.WalletAuditService;
import org.example.core.service.WalletPlayerService;
import org.example.core.service.WalletTransactionsService;

import java.util.*;

import static org.example.core.domain.types.ActionType.VIEW_TRANSACTION_HISTORY;
import static org.example.core.domain.types.AuditType.*;

public class TransactionManager {
    private PlayerManager playerManager;

    private WalletPlayerService playerService = WalletPlayerService.getInstance();
    private WalletTransactionsService transactionsService = WalletTransactionsService.getInstance();
    private WalletAuditService auditService = WalletAuditService.getInstance();

    public TransactionManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    /**
     * The method looks at the audit of the players' actions.
     *
     * @return The number of records in the audit.
     */
    public int viewAllAudits() {
        List<Audit> audits = auditService.findAll();
        for (Audit audit : audits) {
            String formattedOutput = String.format("%-20s | %-15s | %-15s | %d",
                    audit.getPlayerFullName(),
                    audit.getAuditType(),
                    audit.getActionType(),
                    audit.getId());
            System.out.println(formattedOutput);
        }
        return audits.size();
    }

    /**
     * The method looks at the transaction history for the player with the specified name.
     *
     * @param username The player's name.
     */
    public int viewTransactionHistory(String username) {
        Optional<Player> optionalPlayer = playerService.findByUsername(username);

        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            List<Transaction> transactionsList = transactionsService.findByPlayerId(player.getId());

            if (transactionsList.isEmpty()) {
                System.out.println("У пользователя " + username + " нет истории транзакций.");
                playerManager.audit(username, VIEW_TRANSACTION_HISTORY, FAIL);
                return 0;
            }

            System.out.println("Custom ID | Transaction Type  | Amount");
            System.out.println("-----------------------------------");

            for (Transaction transaction : transactionsList) {
                System.out.printf("%-10s | %-16s | %.2f%n",
                        transaction.getCustomId(),
                        transaction.getType(),
                        transaction.getAmount());
            }
            playerManager.audit(username, VIEW_TRANSACTION_HISTORY, SUCCESS);
            return transactionsList.size();
        } else {
            System.out.println("Пользователь с именем " + username + " не найден.");
            playerManager.audit(username, VIEW_TRANSACTION_HISTORY, FAIL);
            return 0;
        }
    }
}
