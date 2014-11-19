package de.tudresden.swt14ws18.bank;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private double balance = 0;
    private List<Transaction> transactions = new ArrayList<>();

    public boolean outgoingTransaction(BankAccount to, double amount) {
	if (amount < 0)
	    throw new IllegalArgumentException("amount must be greater than 0!");

	if (balance < amount)
	    return false; 

	balance -= amount;
	Transaction trans = new Transaction(this, to, amount);
	transactions.add(trans);
	to.incomingTransaction(trans);
	return true;
    }

    public void incomingTransaction(Transaction trans) {
	balance += trans.getAmount();
	transactions.add(trans);
    }

    public double getBalance() {
	return balance;
    }

    public List<Transaction> getTransactions() {
	return transactions;
    }

    public boolean hasBalance(double value) {
	return balance >= value;
    }
}
