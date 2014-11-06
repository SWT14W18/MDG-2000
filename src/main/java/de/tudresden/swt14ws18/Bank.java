package de.tudresden.swt14ws18;

import java.util.ArrayList;
import java.util.List;

public class Bank {
	private List<BankAccount> accounts = new ArrayList<>();
	
	public Bank()
	{
		
	}
	
	public BankAccount createAccount()
	{
		BankAccount acc = new BankAccount();
		
		accounts.add(acc);
		return acc;
	}
	
	public List<BankAccount> getAccounts()
	{
		return accounts;
	}
	
	public List<Transaction> getTransactions()
	{
		List<Transaction> trans = new ArrayList<>();
		
		for(BankAccount acc : accounts)
			trans.addAll(acc.getTransactions());
		
		return trans;
	}
}
