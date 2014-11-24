package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Customer {
	
	private String name;
	private String password;
	
	@Deprecated
	protected Customer() {
	}
	
	public Customer(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPassword(){
		return password;
	}
}
