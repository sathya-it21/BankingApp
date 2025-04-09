package BankingApp;
import java.util.*;

public class BankApp {
    private static HashMap<String, String> credentials = new HashMap<>();
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-register users
        credentials.put("customer1", "pass1");
        credentials.put("customer2", "pass2");
        accounts.put("customer1", new Account(2000.00));
        accounts.put("customer2", new Account(3000.00));

        System.out.println("=== Welcome to the Bank Console App ===");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();

        if (credentials.containsKey(username)) {
            System.out.println("Username already taken.");
            return;
        }

        System.out.print("Set password: ");
        String password = scanner.nextLine();

        System.out.print("Initial deposit amount: ");
        double deposit = Double.parseDouble(scanner.nextLine());

        credentials.put(username, password);
        accounts.put(username, new Account(deposit));
        System.out.println("Registration successful! You can now log in.");
    }

    private static void loginUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (credentials.containsKey(username) && credentials.get(username).equals(password)) {
            System.out.println("Login successful!\n");
            Account userAccount = accounts.get(username);
            userMenu(userAccount);
        } else {
            System.out.println("Invalid login.");
        }
    }

    // Post-login menu
    private static void userMenu(Account account) {
        while (true) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Set annual interest rate");
            System.out.println("2. View balance");
            System.out.println("3. View & apply monthly interest");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new annual interest rate (e.g., 0.05 for 5%): ");
                    double rate = Double.parseDouble(scanner.nextLine());
                    Account.setAnnualInterestRate(rate);
                    System.out.println("Interest rate updated successfully.");
                }
                case 2 -> account.displayBalance();
                case 3 -> {
                    account.calculateMonthlyInterest();
                    System.out.println("Monthly interest applied.");
                    account.displayBalance();
                }
                case 4 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
