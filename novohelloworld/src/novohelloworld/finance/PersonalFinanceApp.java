package novohelloworld.finance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Very small console-based personal finance application.
 */
public class PersonalFinanceApp {
    private final List<Account> accounts = new ArrayList<>();
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
                        editAccount(scanner);
                        break;
                    case "7":
                        archiveAccount(scanner);
                        break;
                    case "8":
                        reorderAccounts(scanner);
                        break;
                    case "9":
                        createCreditCard(scanner);
                        break;
                    case "10":
                        addCreditCardExpense(scanner);
                        break;
                    case "11":
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
        System.out.println("6) Edit account");
        System.out.println("7) Archive account");
        System.out.println("8) Reorder accounts");
        System.out.println("9) Create credit card");
        System.out.println("10) Add credit card expense");
        System.out.println("11) View credit card summary");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private void createAccount(Scanner scanner) {
        System.out.print("Account name: ");
        String name = scanner.nextLine();
        if (findAccountByName(name) != null) {
            System.out.println("Account already exists");
            return;
        }

        System.out.print("Account type (BANK, CREDIT_CARD, CASH, INVESTMENT): ");
        String typeStr = scanner.nextLine().toUpperCase();
        Account.AccountType type;
        try {
            type = Account.AccountType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type");
            return;
        }

        System.out.print("Currency (e.g. USD): ");
        String currency = scanner.nextLine();

        System.out.print("Opening balance: ");
        double opening = Double.parseDouble(scanner.nextLine());

        System.out.print("Color: ");
        String color = scanner.nextLine();

        accounts.add(new Account(name, type, currency, color, opening));
        System.out.println("Account created");
    }

    private void addTransaction(Scanner scanner, Transaction.Type type) {
        System.out.print("Account name: ");
        String accountName = scanner.nextLine();
        Account account = findAccountByName(accountName);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        if (account.isArchived()) {
            System.out.println("Account is archived");
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
        int index = 1;
        for (Account acc : accounts) {
            String archivedFlag = acc.isArchived() ? "(archived)" : "";
            System.out.printf("%d) %s %s - Balance: %.2f %s%n", index++, acc.getName(), archivedFlag, acc.getBalance(), acc.getCurrency());
        }
    }

    private void listTransactions(Scanner scanner) {
        System.out.print("Account name: ");
        String accountName = scanner.nextLine();
        Account account = findAccountByName(accountName);
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

    private void editAccount(Scanner scanner) {
        System.out.print("Account name to edit: ");
        String name = scanner.nextLine();
        Account account = findAccountByName(name);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        System.out.print("New name (leave blank to keep): ");
        String newName = scanner.nextLine();
        if (!newName.isBlank() && findAccountByName(newName) == null) {
            account.setName(newName);
        }
        System.out.print("New color (leave blank to keep): ");
        String color = scanner.nextLine();
        if (!color.isBlank()) {
            account.setColor(color);
        }
        System.out.println("Account updated");
    }

    private void archiveAccount(Scanner scanner) {
        System.out.print("Account name to archive: ");
        String name = scanner.nextLine();
        Account account = findAccountByName(name);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        System.out.print("Are you sure? (y/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) {
            return;
        }
        System.out.print("Type ARCHIVE to confirm: ");
        if (scanner.nextLine().equals("ARCHIVE")) {
            account.archive();
            System.out.println("Account archived");
        } else {
            System.out.println("Archive cancelled");
        }
    }

    private void reorderAccounts(Scanner scanner) {
        if (accounts.size() < 2) {
            System.out.println("Not enough accounts to reorder");
            return;
        }
        listAccounts();
        System.out.print("Move which index? ");
        int from = Integer.parseInt(scanner.nextLine()) - 1;
        if (from < 0 || from >= accounts.size()) {
            System.out.println("Invalid index");
            return;
        }
        System.out.print("Move to position: ");
        int to = Integer.parseInt(scanner.nextLine()) - 1;
        if (to < 0 || to >= accounts.size()) {
            System.out.println("Invalid position");
            return;
        }
        Account acc = accounts.remove(from);
        accounts.add(to, acc);
        System.out.println("Accounts reordered");
    }

    private Account findAccountByName(String name) {
        for (Account acc : accounts) {
            if (acc.getName().equalsIgnoreCase(name)) {
                return acc;
            }
        }
        return null;
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
