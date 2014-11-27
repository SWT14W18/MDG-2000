package de.tudresden.swt14ws18.bank;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.time.BusinessTime;

import de.tudresden.swt14ws18.Lotterie;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    private long id;
    
    private LocalDateTime date;

    @OneToOne
    private BankAccount to;
    @OneToOne
    private BankAccount from;
    private double amount;

    @Deprecated
    public Transaction() {}
    
    public Transaction(BankAccount from, BankAccount to, double amount) {
	this.to = to;
	this.from = from;
	this.amount = amount;
	this.date = Lotterie.getInstance().getTime().getTime();

	Lotterie.getInstance().getTransactionRepo().save(this);
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
    
    @Override
    public String toString(){
        String von;
        String nach;
        
        if(from==null) von = "eingezahlt";
        else von = from.toString();
        if(to==null) nach = "ausgezahlt";
        else nach = to.toString();
        
        return "Datum: "+date.toString()+" Von: "+von+" Nach: "+nach;
    }
}
