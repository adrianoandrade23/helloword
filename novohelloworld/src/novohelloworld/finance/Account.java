package novohelloworld.finance;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple account that tracks transactions and balance.
 */
public class Account {
    private final String name;
    private final List<Transaction> transactions = new ArrayList<>();
    private double balance = 0.0;

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        if (t.getType() == Transaction.Type.INCOME) {
            balance += t.getAmount();
        } else {
            balance -= t.getAmount();
        }
    }
}
