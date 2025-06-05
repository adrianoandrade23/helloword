package novohelloworld.finance;

import java.time.LocalDate;

/**
 * Basic transaction record with amount, description and date.
 */
public class Transaction {
    public enum Type { INCOME, EXPENSE }
    public enum PaymentMethod { CASH, CREDIT_CARD }

    private final Type type;
    private final double amount;
    private final String description;
    private final LocalDate date;
    private final PaymentMethod paymentMethod;
    private final LocalDate dueDate;
    private final boolean countsTowardLimit;

    public Transaction(Type type, double amount, String description, LocalDate date) {
        this(type, amount, description, date, PaymentMethod.CASH, date, true);
    }

    public Transaction(Type type, double amount, String description, LocalDate date, PaymentMethod method) {
        this(type, amount, description, date, method, date, true);
    }

    public Transaction(Type type, double amount, String description, LocalDate date, PaymentMethod method, LocalDate dueDate, boolean countsTowardLimit) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.paymentMethod = method;
        this.dueDate = dueDate;
        this.countsTowardLimit = countsTowardLimit;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCountsTowardLimit() {
        return countsTowardLimit;
    }
}
