package de.tudresden.swt14ws18.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.bank.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
	
	

}