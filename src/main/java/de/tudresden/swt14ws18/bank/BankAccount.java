package de.tudresden.swt14ws18.bank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class BankAccount {
    
    @Id
    @GeneratedValue
    private long id;

    
    private double balance = 0;
    @OneToMany
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

    public void payIn(double amount) {
	if(amount < 0)
	    return;
	
	Transaction trans = new Transaction(null, this, amount);
	balance += amount;
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
