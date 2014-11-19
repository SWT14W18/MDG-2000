package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class Customer {

	@Autowired
	
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String password;
	
	public Customer(String name, String password){
		this.name = name;
		this.password = password;
	}
}
