package de.tudresden.swt14ws18.bankTests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;

/**
 * Der BankAccountIntegratioNTest befasst sich mit der BankAccountverwaltung.
 * 
 * getestete Funktionen:
 * 
 * Erstellung, Speicherung im Repository (siehe test dazu), Einzahlung, Transfer, Auszahlung
 * Transaktionen abrufen, Transaktion erstellen
 * 
 * @author Reinhard_2
 *
 */

public class BankAccountTest extends AbstractIntegrationTest{
    
    @Autowired TransactionRepository transRepo;
    @Autowired BankAccountRepository bARepo;

    @Test
    public void testBankAccount() {
        
        
	BankAccount a1 = new BankAccount();
	BankAccount a2 = new BankAccount();
	
	bARepo.save(Arrays.asList(a1,a2));

	assertEquals(a1.getBalance(), 0, 0.001);
	assertEquals(a2.getBalance(), 0, 0.001);
	
	

        assertFalse(a1.outgoingTransaction(a2, 10));
        assertFalse(a2.outgoingTransaction(a1, 10));

        
	a1.incomingTransaction(new Transaction(null ,a1,50.0));
	a2.incomingTransaction(new Transaction(null , a2, 100.0));
	
	
	assertEquals(a1.getBalance(), 50.0, 0.001);
	assertTrue(a1.outgoingTransaction(a2, 10));	

	assertEquals(a1.getBalance(), 40.0, 0.001);
	assertEquals(a2.getBalance(), 110.0, 0.001);
    }
    
    @Test
    public void getTransactions(){
        //Iterable<Transaction> transactions = transRepo.findAll();
        
        BankAccount tempBa = new BankAccount();
        bARepo.save(tempBa);
        
        Transaction tempTrans = new Transaction(null, tempBa,200.0);
        
        assertThat(transRepo.findAll(), hasItem(tempTrans));
    }
}
