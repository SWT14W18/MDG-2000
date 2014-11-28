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

    private String reason;

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
	this(from, to, amount, "");
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
     * @param reason
     *            der Grund für die Überweisung
     */
    public Transaction(BankAccount from, BankAccount to, double amount, String reason) {
	this.to = to;
	this.from = from;
	this.amount = amount;
	this.date = Lotterie.getInstance().getTime().getTime();
	this.reason = reason;

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
       if(from==null) return from; 
       else return Lotterie.getInstance().getCustomerRepository().findByname("einzahlung").getAccount();     
    }

    /**
     * Hole den Account an dem die Transaktion geht
     * 
     * @return der Account an dem die Transaktion geht
     */
    public BankAccount getTo() {
        if(to!=null) return to;
        else return Lotterie.getInstance().getCustomerRepository().findByname("auszahlung").getAccount();
    }

    /**
     * Get the reason for this Transaction. Can be an empty String ("") or null.
     * 
     * @return the reason String
     */
    public String getReason() {
	return reason;
    }

    @Override
    public String toString() {
	String von;
	String nach;
	String betrag;
	
	betrag = String.valueOf(amount);

	if (from == null)
	    von = "eingezahlt";
	else
	    von = from.toString();
	if (to == null)
	    nach = "ausgezahlt";
	else
	    nach = to.toString();

	return "Datum: " + date.toString() + " Von: " + von + " Nach: " + nach+" Geldbetrag: "+betrag+"€";
    }
}
