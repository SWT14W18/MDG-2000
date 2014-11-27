package de.tudresden.swt14ws18.bank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.tudresden.swt14ws18.Lotterie;

/**
 * Repräsentiert einen Bankaccount
 */
@Entity
public class BankAccount {
    @Id
    @GeneratedValue
    private long id;

    private double balance = 0;
    @OneToMany
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Erstellt eine Transaktion die von diesem Account ausgeht.
     * Das Geld wird dabei von diesem Account abgezogen und dem übergebenen Account überwiesen.
     * 
     * Transaktionen zu null werden als Auszahlung definiert!
     * 
     * @param to der Zielaccount auf dem das Geld überwiesen werden soll, null für auszahlung
     * @param amount der Geldbetrag der überwiesen werden soll
     * @return true wenn die Transaktion erfolgreich war, false wenn nicht
     */
    public boolean outgoingTransaction(BankAccount to, double amount) {
	if (amount < 0)
	    throw new IllegalArgumentException("amount must be greater than 0!");

	if (balance < amount)
	    return false;

	balance -= amount;
	Transaction trans = new Transaction(this, to, amount);
	transactions.add(trans);
	if (to != null)
	    to.incomingTransaction(trans);
	return true;
    }

    /**
     * Behandelt eine ankommende Transaktion, sprich fügt das Geld zum Kontostand hinzu.
     * 
     * @param trans die ankommende Transaktion
     */
    public void incomingTransaction(Transaction trans) {
	balance += trans.getAmount();
	transactions.add(trans);
    }

    /**
     * Zahle einen Betrag in das Konto ein. Es wird angenommen, dass das Geld in einer Annahmestelle eingezahlt wird.
     * 
     * @param amount der einzuzahlende Betrag
     */
    public void payIn(double amount) {
	if (amount < 0)
	    return;

	Transaction trans = new Transaction(null, this, amount);
	balance += amount;
	transactions.add(trans);
    }

    /**
     * Hole den momentanen Kontostand dieses Accounts
     * 
     * @return der Kontostand als double
     */
    public double getBalance() {
	return balance;
    }

    /**
     * Hole die Liste aller Transaktionen in denen dieser BankAccount verwickelt ist.
     * 
     * @return eine Liste aller Transaktionen
     */
    public List<Transaction> getTransactions() {
	return transactions;
    }

    /**
     * Überprüfe ob der BankAccount das nötige Geld hat.
     * 
     * @param value der Geldbetrag der überprüft werden soll
     * @return true wenn der Account genug Geld hat, false wenn nicht
     */
    public boolean hasBalance(double value) {
	return balance >= value;
    }
    
    public String getOwnerName(){
        return Lotterie.getInstance().getCustomerRepository().findByAccount(this).getName();
    }
}
