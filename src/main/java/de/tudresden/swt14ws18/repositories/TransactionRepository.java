package de.tudresden.swt14ws18.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public List<Transaction> findByFromOrToOrderByDateDesc(BankAccount customer, BankAccount customer2);

    public List<Transaction> findByFrom(BankAccount customer);

    public List<Transaction> findByTo(BankAccount bankAccount);
}