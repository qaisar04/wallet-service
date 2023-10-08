package org.example.entity;

/**
 * Класс, представляющий транзакцию.
 */
public class Transaction {
    private String id;
    private double amount;
    private String type;

    /**
     * Конструктор для создания объекта Transaction.
     *
     * @param id     Уникальный идентификатор транзакции.
     * @param type   Тип транзакции ("debit" или "credit").
     * @param amount Сумма транзакции.
     */
    public Transaction(String id, String type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    /**
     * Метод для получения идентификатора транзакции.
     */
    public String getId() {
        return id;
    }

    /**
     * Метод для получение суммы
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Метод для получения тип транзакции ("debit" или "credit").
     */
    public String getType() {
        return type;
    }
}
