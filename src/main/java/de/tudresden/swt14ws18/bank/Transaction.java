package de.tudresden.swt14ws18.bank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Transaction {
	
	@Id
	@GeneratedValue
	private long id;
	
	@OneToOne
    private BankAccount to;
    @OneToOne
    private BankAccount from;
    private double amount;

    public Transaction(BankAccount from, BankAccount to, double amount) {
	this.to = to;
	this.from = from;
	this.amount = amount;
    }

    public double getAmount() {
	return amount;
    }

    public BankAccount getFrom() {
	return from;
    }

    public BankAccount getTo() {
	return to;
    }
}
