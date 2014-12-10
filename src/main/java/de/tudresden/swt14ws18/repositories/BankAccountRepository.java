package de.tudresden.swt14ws18.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.bank.BankAccount;

@Configuration
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
	
	

}