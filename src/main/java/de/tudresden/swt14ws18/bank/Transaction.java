package de.tudresden.swt14ws18.bank;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.tudresden.swt14ws18.Lotterie;

/**
 * Repräsentiert eine Geld Transaktion zwischen 2 Kunden, wobei null Accounts einen physischen Kunden repräsentieren.
 */
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
    public Transaction() {
    }

    /**
     * Erstellt eine Transaktion und speichert diese.
     * 
     * @param from
     *            der Account von dem die Transaktion ausgeht
     * @param to
     *            der Account an dem die Transaktion geht
     * @param amount
     *            der Betrag der überwiesen wird
     */
    public Transaction(BankAccount from, BankAccount to, double amount) {
	this.to = to;
	this.from = from;
	this.amount = amount;
	this.date = Lotterie.getInstance().getTime().getTime();

	Lotterie.getInstance().getTransactionRepo().save(this);
    }

    /**
     * Hole den Betrag des überwiesenen Geldes
     * 
     * @return der Betrag als double
     */
    public double getAmount() {
	return amount;
    }

    /**
     * Hole den Account von dem die Transaktion ausgeht
     * 
     * @return der Account von dem die Transaktion ausgeht
     */
    public BankAccount getFrom() {
	return from;
    }

    /**
     * Hole den Account an dem die Transaktion geht
     * 
     * @return der Account an dem die Transaktion geht
     */
    public BankAccount getTo() {
	return to;
    }

    @Override
    public String toString() {
	String von;
	String nach;

	if (from == null)
	    von = "eingezahlt";
	else
	    von = from.toString();
	if (to == null)
	    nach = "ausgezahlt";
	else
	    nach = to.toString();

	return "Datum: " + date.toString() + " Von: " + von + " Nach: " + nach;
    }
}
