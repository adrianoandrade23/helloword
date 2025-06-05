package novohelloworld.finance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Very small console-based personal finance application.
 */
public class PersonalFinanceApp {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, CreditCardAccount> creditCards = new HashMap<>();

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                for (CreditCardAccount cc : creditCards.values()) {
                    cc.checkNotifications();
                }
                printMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        createAccount(scanner);
                        break;
                    case "2":
                        addTransaction(scanner, Transaction.Type.INCOME);
                        break;
                    case "3":
                        addTransaction(scanner, Transaction.Type.EXPENSE);
                        break;
                    case "4":
                        listAccounts();
                        break;
                    case "5":
                        listTransactions(scanner);
                        break;
                    case "6":
                        createCreditCard(scanner);
                        break;
                    case "7":
                        addCreditCardExpense(scanner);
                        break;
                    case "8":
                        viewCreditCardSummary(scanner);
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Unknown option");
                }
            }
        }
    }

    private void printMenu() {
        System.out.println("== Finance App ==");
        System.out.println("1) Create account");
        System.out.println("2) Add income");
        System.out.println("3) Add expense");
        System.out.println("4) List accounts");
        System.out.println("5) List transactions for account");
        System.out.println("6) Create credit card");
        System.out.println("7) Add credit card expense");
        System.out.println("8) View credit card summary");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private void createAccount(Scanner scanner) {
        System.out.print("Account name: ");
        String name = scanner.nextLine();
        if (accounts.containsKey(name)) {
            System.out.println("Account already exists");
            return;
        }
        accounts.put(name, new Account(name));
        System.out.println("Account created");
    }

    private void addTransaction(Scanner scanner, Transaction.Type type) {
        System.out.print("Account name: ");
        String accountName = scanner.nextLine();
        Account account = accounts.get(accountName);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        account.addTransaction(new Transaction(type, amount, desc, LocalDate.now()));
        System.out.println("Transaction added");
    }

    private void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts");
            return;
        }
        for (Account acc : accounts.values()) {
            System.out.printf("%s - Balance: %.2f%n", acc.getName(), acc.getBalance());
        }
    }

    private void listTransactions(Scanner scanner) {
        System.out.print("Account name: ");
        String accountName = scanner.nextLine();
        Account account = accounts.get(accountName);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        if (account.getTransactions().isEmpty()) {
            System.out.println("No transactions");
            return;
        }
        for (Transaction t : account.getTransactions()) {
            System.out.printf("%s %s %.2f - %s%n", t.getDate(), t.getType(), t.getAmount(), t.getDescription());
        }
    }

    private void createCreditCard(Scanner scanner) {
        System.out.print("Card name: ");
        String name = scanner.nextLine();
        if (creditCards.containsKey(name)) {
            System.out.println("Card already exists");
            return;
        }
        System.out.print("Statement cycle day (1-28): ");
        int cycleDay = Integer.parseInt(scanner.nextLine());
        System.out.print("Payment due day (1-28): ");
        int dueDay = Integer.parseInt(scanner.nextLine());
        System.out.print("Credit limit: ");
        double limit = Double.parseDouble(scanner.nextLine());
        System.out.print("Interest rate APR (%): ");
        double apr = Double.parseDouble(scanner.nextLine());
        creditCards.put(name, new CreditCardAccount(name, cycleDay, limit, dueDay, apr));
        System.out.println("Credit card created");
    }

    private void addCreditCardExpense(Scanner scanner) {
        System.out.print("Card name: ");
        String name = scanner.nextLine();
        CreditCardAccount card = creditCards.get(name);
        if (card == null) {
            System.out.println("Card not found");
            return;
        }
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Installments (1 for none): ");
        int inst = Integer.parseInt(scanner.nextLine());
        card.addTransaction(amount, desc, inst);
        System.out.println("Transaction added");
    }

    private void viewCreditCardSummary(Scanner scanner) {
        System.out.print("Card name: ");
        String name = scanner.nextLine();
        CreditCardAccount card = creditCards.get(name);
        if (card == null) {
            System.out.println("Card not found");
            return;
        }
        card.printSummary();
    }
}
