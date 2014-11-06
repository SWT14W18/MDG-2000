package de.tudresden.swt14ws18;

public class Transaction {
	private BankAccount to;
	private BankAccount from;
	private double amount;
	
	public Transaction(BankAccount from, BankAccount to, double amount)
	{
		this.to = to;
		this.from = from;
		this.amount = amount;
	}
	
	public double getAmount()
	{
		return amount;
	}
	
	public BankAccount getFrom()
	{
		return from;
	}
	
	public BankAccount getTo()
	{
		return to;
	}
}
