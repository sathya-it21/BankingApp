package com.banking_application;


public class SavingsAccount {
	private static double annualInterestRate;
	private double savingsBalance;
	
	
	public SavingsAccount(double savingsBalance){
		this.savingsBalance = savingsBalance;
	}
	
	
	//Getter and Setter Methods
	public static double getAnnualInterestRate() {
		return annualInterestRate;
	}
	public static void setAnnualInterestRate(double annualInterestRate) {
		SavingsAccount.annualInterestRate = annualInterestRate;
	}

	public double getSavingsBalance() {
		return savingsBalance;
	}
	public void setSavingsBalance(double savingsBalance) {
		this.savingsBalance = savingsBalance;
	}
	
	
	
	
	//Method to Modify Interest Rate
	public static void modifyInterestRate(double newAnnualInterestRate) {
		SavingsAccount.annualInterestRate = newAnnualInterestRate;
	}
	
	

	//Method to just calculate Monthly Interest without updating Savings balance
	public double calculateMonthlyInterest() {
		double monthlyInterestRate = annualInterestRate /100;
		double monthlyInterest = (this.getSavingsBalance() * monthlyInterestRate)/12;
		return monthlyInterest;
	}
	
	
	//Method to Calculate Monthly Interest and Update Savings Balance
	public double updateBalanceWithInterest(){
		double monthlyInterest = calculateMonthlyInterest();
		this.setSavingsBalance(this.getSavingsBalance() + monthlyInterest);
		return monthlyInterest;
	}
}
