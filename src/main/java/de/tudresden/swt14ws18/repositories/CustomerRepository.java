package de.tudresden.swt14ws18.repositories;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface CustomerRepository extends CrudRepository<ConcreteCustomer, Long> {
	
	public ConcreteCustomer findByUserAccount(UserAccount userAccount);
	public ConcreteCustomer findByAccount(BankAccount bankAccount);
	public ConcreteCustomer findByname(String name);

}
