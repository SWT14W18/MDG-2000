package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.Entity;

@Entity
public class ConcreteCustomer extends Customer{
	
	//private Account account;
	private int messages;
	private enum status {
		ACTIVE, CLOSED, ANONYM
	}
	
	public ConcreteCustomer(String name, String password) {
		super(name, password);
	}
	
	public void payMessage(){
		//TODO: 
	}
	
/*	public void joinGroup( Community community){
		
	}*/

	public void leaveGroup(Community community){
		//TODO:
	}
	
	public Account getAccount(){
		//TODO:
	}
}
