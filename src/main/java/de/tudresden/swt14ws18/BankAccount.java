package de.tudresden.swt14ws18;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
	private double balance;
	private List<Transaction> transactions = new ArrayList<>();

	public void outgoingTransaction(BankAccount to, double amount) {
		if (amount < 0)
			throw new IllegalArgumentException("amount must be greater than 0!");

		if (balance < amount)
			throw new IllegalArgumentException(
					"This account does not have enough money");

		balance -= amount;

		Transaction trans = new Transaction(this, to, amount);
		transactions.add(trans);
		to.incomingTransaction(trans);
	}

	protected void incomingTransaction(Transaction trans) {
		balance += trans.getAmount();
		transactions.add(trans);
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
}
