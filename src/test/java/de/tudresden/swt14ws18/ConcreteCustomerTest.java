package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;

public class ConcreteCustomerTest {

	private UserAccount userAccount;
	private BankAccount bankAccount;
	
	@Test
	public void testConcreteCustomer() {
		ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter","234", Status.ACTIVE, userAccount, bankAccount);
		ConcreteCustomer Customer2 = new ConcreteCustomer("Paul","567", Status.ANONYM, userAccount, bankAccount);
		
		assertEquals(Customer1.getName(),"Dieter"); 
		assertEquals(Customer2.getName(), "Paul");
		
		assertEquals(Customer1.getState(), Status.ACTIVE);
		assertEquals(Customer2.getState(), Status.ANONYM);
	}

}
