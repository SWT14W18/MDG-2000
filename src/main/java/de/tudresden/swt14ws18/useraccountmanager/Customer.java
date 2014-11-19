package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class Customer {
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String password;
	
	@Deprecated
	protected Customer() {
	}
	
	public Customer(String name, String password){
		this.name = name;
		this.password = password;
	}
}
