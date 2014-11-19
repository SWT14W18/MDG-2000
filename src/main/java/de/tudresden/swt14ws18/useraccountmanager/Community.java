package de.tudresden.swt14ws18.useraccountmanager;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

public class Community extends Customer{

	private Set<ConcreteCustomer> members = new HashSet<>();
	@OneToOne
	private UserAccount userAccount;
	
	public Community(String name, String password) {
		super(name, password);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isMember(ConcreteCustomer concreteCustomer){
		if(members.contains(concreteCustomer)){
			return true;
		}
		return false;
	}

}
