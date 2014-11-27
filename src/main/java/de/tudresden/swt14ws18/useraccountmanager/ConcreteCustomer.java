package de.tudresden.swt14ws18.useraccountmanager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.tips.LottoTipCollection;
import de.tudresden.swt14ws18.tips.TipCollection;
import de.tudresden.swt14ws18.tips.TotoTipCollection;

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
 * @param messages: Anzahl der MItteilungen, die der Kunde bekommen hat, weil er irgendwann nicht liquid war
 * 					Bei 10 Mitteilungen wird der Kunde gesperrt.
 * @param account:	Das Bankkonto des Kunden, auf dem sein Geld liegt. Er kann abheben und einzahlen
 * 
 * @param UserAccount: der eindeutig zugeordnete UserAccount des Kunden
 * 
 * @param state:		Der Status des Kunden (ACTIVE, CLOSED, ANONYM)
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
    
    @ElementCollection
    private List<LottoTipCollection> lottoTips = new ArrayList<>();
    @ElementCollection
    private List<TotoTipCollection> totoTips = new ArrayList<>();

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

    public int getMessageCount() {
	return messages;
    }

    public void addMessage() {
	messages++;
    }

    public void payOneMessage() {
    	account.outgoingTransaction(Lotterie.getInstance().getBankAccount(), 2);
    	messages--;
    }
    
    public void payAllMessages(){
    	int i=0;
    	
    	for(i=0;i<=messages;i++){
    		account.outgoingTransaction(Lotterie.getInstance().getBankAccount(), 2);	
    	}
    }

    
     public void joinGroup( Community community){
     //TODO
     }
    

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

    public List<TipCollection<?>> getTips() {
	List<TipCollection<?>> tips = new ArrayList<>();
	tips.addAll(lottoTips);
	tips.addAll(totoTips);
	return tips;
    }

    public void addLottoTips(LottoTipCollection craftLottoTips) {
	lottoTips.add(craftLottoTips);
    }

    public void addTotoTips(TotoTipCollection craftTotoTips) {
	totoTips.add(craftTotoTips);
    }
}
