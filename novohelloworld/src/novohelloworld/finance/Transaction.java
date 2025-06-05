package novohelloworld.finance;

import java.time.LocalDate;

/**
 * Basic transaction record with amount, description and date.
 */
public class Transaction {
    public enum Type { INCOME, EXPENSE }

    private final Type type;
    private final double amount;
    private final String description;
    private final LocalDate date;

    public Transaction(Type type, double amount, String description, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
}
