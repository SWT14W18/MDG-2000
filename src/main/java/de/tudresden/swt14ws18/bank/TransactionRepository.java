package de.tudresden.swt14ws18.bank;

import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
	

}