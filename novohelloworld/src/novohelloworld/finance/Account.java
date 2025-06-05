package novohelloworld.finance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple account that tracks transactions and balance.
 */
public class Account {
    public enum AccountType { BANK, CREDIT_CARD, CASH, INVESTMENT }

    private static int nextId = 1;

    private final int id;
    private String name;
    private final AccountType type;
    private final String currency;
    private String color;
    private final List<Transaction> transactions = new ArrayList<>();
    private final double openingBalance;
    private double balance = 0.0;
    private boolean isArchived = false;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(String name, AccountType type, String currency, String color, double openingBalance) {
        this.id = nextId++;
        this.name = name;
        this.type = type;
        this.currency = currency;
        this.color = color;
        this.openingBalance = openingBalance;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (openingBalance != 0.0) {
            Transaction init = new Transaction(Transaction.Type.INCOME, openingBalance, "Initial Balance", LocalDate.now());
            addTransaction(init);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public AccountType getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void archive() {
        isArchived = true;
        this.updatedAt = LocalDateTime.now();
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        if (t.getType() == Transaction.Type.INCOME) {
            balance += t.getAmount();
        } else {
            balance -= t.getAmount();
        }
        this.updatedAt = LocalDateTime.now();
    }
}
