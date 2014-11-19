package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.bank.BankAccount;

@Entity
public class ConcreteCustomer extends Customer{
	
	//private Account account;
	private int messages;
	public Status state;
	
	@OneToOne
	private UserAccount userAccount;
	
	private CustomerRepository repository;
	private BankAccount account;
	
	@Deprecated
	protected ConcreteCustomer() {
	}
	
	public ConcreteCustomer(String name, String password, Status state, UserAccount userAccount) {
		super(name, password);
		this.userAccount = userAccount;
		this.state = state;
		this.account = new BankAccount();
		repository.save(this);
	}
	
	public void setStatus(Status state){
		this.state = state;
	}
	
	public Status getState(){
		return state;
	}
	
	public int countMessages(){
		return messages;
	}
	
	public void payMessage(){
		//TODO: 
	}
	
/*	public void joinGroup( Community community){
		
	}*/

	public void leaveGroup(Community community){
		//TODO:
	}
	
	public BankAccount getAccount(){
	    return account;
	}
}
