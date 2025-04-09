package BankingApp;

public class Account {
    private double balance;
    private static double annualInterestRate;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    public void calculateMonthlyInterest() {
        double interest = (balance * annualInterestRate) / 12;
        balance += interest;
    }

    public double getBalance() {
        return balance;
    }

    public void displayBalance() {
        System.out.printf("Current Balance: $%.2f%n", balance);
    }

    public static void setAnnualInterestRate(double rate) {
        annualInterestRate = rate;
    }
}

