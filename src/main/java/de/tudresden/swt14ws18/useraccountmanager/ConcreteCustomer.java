package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.bank.BankAccount;

/**
 * Wie schon aus dem Header zu entnehmen, ist diese Klasse von der Klasse "Customer" abgeleitet, welche
 * den ConcreteCustomer mit einem Namen udn einem Passwort ausstattet.
 * Der ConcreteCustomer ist die aktive, agierende Kundenklasse in unserer Lotterie, der als Entity
 * von Spring gefunden und deshalb eine von Spring verwaltete ID besitzt.
 * 
 * Dem Kunden ist genau ein org.salespointframework.useraccount.UserAccount zugeordnet, über den er später
 * in der Lotterie zu identifizieren ist.
 * Um den Kunden allerdings in unserer Lotterie (persistent) zu speichern, ist ein CustomerRepository
 * angelegt, in dem Jeder Kunde gespeichert ist.
 * 
 * @author Reinhard_2
 *
 */

@Entity
public class ConcreteCustomer extends Customer {

    @Id
    @GeneratedValue
    private long id;

    private int messages = 0;
    public Status state;

    @OneToOne
    private UserAccount userAccount;

    @OneToOne
    private BankAccount account;

    @Deprecated
    protected ConcreteCustomer() {
    }

    public ConcreteCustomer(String name, String password, Status state, UserAccount userAccount, BankAccount bankAccount) {
	super(name, password);
	this.userAccount = userAccount;
	this.state = state;
	this.account = bankAccount;

    }

    public void setStatus(Status state) {
	this.state = state;
    }

    public Status getState() {
	return state;
    }

    public int countMessages() {
	return messages;
    }

    public void addMessage() {
	messages++;
    }

    public void payMessage() {
	// TODO:
    }

    /*
     * public void joinGroup( Community community){
     * 
     * }
     */

    public void leaveGroup(Community community) {
	// TODO:
    }

    public BankAccount getAccount() {
	return account;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ConcreteCustomer other = (ConcreteCustomer) obj;
	if (id != other.id)
	    return false;
	return true;
    }
}
