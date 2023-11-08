package kz.baltabayev.audits.manager;

public interface TransactionManager<T, V> {
    public V viewAllAudits(T token);

    public V viewTransactionHistory(T token);
}
