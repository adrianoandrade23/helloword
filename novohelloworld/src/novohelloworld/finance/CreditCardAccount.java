package novohelloworld.finance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic credit card account with statement and limit tracking.
 */
public class CreditCardAccount {
    private final String name;
    private final int statementCycleDay;
    private final double creditLimit;
    private final int paymentDueDay;
    private final double interestRateAPR;
    private final List<Transaction> transactions = new ArrayList<>();

    public CreditCardAccount(String name, int statementCycleDay, double creditLimit, int paymentDueDay, double interestRateAPR) {
        this.name = name;
        this.statementCycleDay = statementCycleDay;
        this.creditLimit = creditLimit;
        this.paymentDueDay = paymentDueDay;
        this.interestRateAPR = interestRateAPR;
    }

    public String getName() {
        return name;
    }

    public int getStatementCycleDay() {
        return statementCycleDay;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public int getPaymentDueDay() {
        return paymentDueDay;
    }

    public double getInterestRateAPR() {
        return interestRateAPR;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    /**
     * Adds an expense in installments. Each installment becomes a transaction.
     * Only the first installment counts toward the credit limit.
     */
    public void addTransaction(double amount, String description, int installments) {
        LocalDate now = LocalDate.now();
        for (int i = 0; i < installments; i++) {
            boolean counts = i == 0;
            LocalDate dueDate = now.plusMonths(i + 1).withDayOfMonth(paymentDueDay);
            Transaction t = new Transaction(
                    Transaction.Type.EXPENSE,
                    amount / installments,
                    installments > 1 ? description + " (" + (i + 1) + "/" + installments + ")" : description,
                    now,
                    Transaction.PaymentMethod.CREDIT_CARD,
                    dueDate,
                    counts
            );
            addTransaction(t);
        }
    }

    public LocalDate getStatementClosingDate() {
        LocalDate today = LocalDate.now();
        LocalDate closing = LocalDate.of(today.getYear(), today.getMonth(), statementCycleDay);
        if (today.getDayOfMonth() > statementCycleDay) {
            closing = closing.plusMonths(1);
        }
        return closing;
    }

    public LocalDate getStatementPeriodStart() {
        return getStatementClosingDate().minusMonths(1).plusDays(1);
    }

    public LocalDate getPaymentDueDate() {
        return getStatementClosingDate().plusMonths(1).withDayOfMonth(paymentDueDay);
    }

    public double getCurrentSpend() {
        LocalDate start = getStatementPeriodStart();
        LocalDate end = getStatementClosingDate();
        double total = 0;
        for (Transaction t : transactions) {
            if (t.getPaymentMethod() == Transaction.PaymentMethod.CREDIT_CARD
                    && t.isCountsTowardLimit()
                    && !t.getDate().isBefore(start)
                    && !t.getDate().isAfter(end)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public double getAvailableLimit() {
        return creditLimit - getCurrentSpend();
    }

    public double getUtilizationRatio() {
        return getCurrentSpend() / creditLimit;
    }

    public void printSummary() {
        System.out.println("== Credit Card " + name + " ==");
        System.out.printf("Statement period: %s - %s%n", getStatementPeriodStart(), getStatementClosingDate());
        System.out.printf("Closing date: %s%n", getStatementClosingDate());
        System.out.printf("Payment due date: %s%n", getPaymentDueDate());
        System.out.printf("Current spend: %.2f%n", getCurrentSpend());
        System.out.printf("Available limit: %.2f%n", getAvailableLimit());
        System.out.printf("Utilization ratio: %.2f%n", getUtilizationRatio());
    }

    public void checkNotifications() {
        LocalDate today = LocalDate.now();
        if (today.equals(getStatementClosingDate().minusDays(1))) {
            System.out.printf("Reminder: Statement closes tomorrow for card %s%n", name);
        }
        if (today.equals(getPaymentDueDate())) {
            System.out.printf("Reminder: Payment due today for card %s%n", name);
        }
    }
}
