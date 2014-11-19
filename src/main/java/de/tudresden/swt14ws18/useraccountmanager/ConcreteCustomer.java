package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

@Entity
public class ConcreteCustomer extends Customer{
	
	//private Account account;
	private int messages;
	private enum Status {
		ACTIVE, CLOSED, ANONYM
	}
	@OneToOne
	private UserAccount userAccount;
	
	public ConcreteCustomer(String name, String password, Status state, UserAccount userAccount) {
		super(name, password);
		this.userAccount = userAccount;
	}
	
	public void payMessage(){
		//TODO: 
	}
	
/*	public void joinGroup( Community community){
		
	}*/

	public void leaveGroup(Community community){
		//TODO:
	}
	
	/*public Account getAccount(){
		//TODO:
	}*/
}
