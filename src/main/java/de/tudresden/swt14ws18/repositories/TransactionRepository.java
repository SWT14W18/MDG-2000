package de.tudresden.swt14ws18.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.bank.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	

}