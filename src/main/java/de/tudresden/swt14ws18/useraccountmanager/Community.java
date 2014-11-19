package de.tudresden.swt14ws18.useraccountmanager;

import java.util.HashSet;
import java.util.Set;

public class Community extends Customer{

	private Set<ConcreteCustomer> members = new HashSet<>();
	
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
