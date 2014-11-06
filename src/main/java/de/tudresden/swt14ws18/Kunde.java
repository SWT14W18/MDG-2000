package de.tudresden.swt14ws18;

import org.salespointframework.useraccount.Password;

public class Kunde {
	private String name;
	private long id;
	private Password password;
	private BankAccount account;
	
	public Kunde(String name, String password)
	{
		this.name = name;
		this.password = new Password(password);
	}
	
	public BankAccount getAccount()
	{
		return account;
	}
	
	public String getName()
	{
		return name;
	}
	
	public long getId()
	{
		return id;
	}
}
