package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;

public class BankAccountTest {

    @Test
    public void testBankAccount() {
	BankAccount a1 = new BankAccount();
	BankAccount a2 = new BankAccount();

	assertEquals(a1.getBalance(), 0, 0.001);
	assertEquals(a2.getBalance(), 0, 0.001);

	assertFalse(a1.outgoingTransaction(a2, 10));
	
	a1.incomingTransaction(new Transaction(null, a1, 100));
	
	assertEquals(a1.getBalance(), 100, 0.001);
	assertTrue(a1.outgoingTransaction(a2, 10));	

	assertEquals(a1.getBalance(), 90, 0.001);
	assertEquals(a2.getBalance(), 10, 0.001);
    }
}
