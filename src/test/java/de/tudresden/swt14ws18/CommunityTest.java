package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;

public class CommunityTest {
	
	private UserAccount userAccount1;
	private UserAccount userAccount2;
	private UserAccount userAccount3;

	@Test
	public void test() {
		ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter","234", Status.ACTIVE, userAccount1);
		ConcreteCustomer Customer2 = new ConcreteCustomer("Paul","567", Status.ANONYM, userAccount2);
		
		Community comm1 = new Community("Lottogruppe","passt", userAccount3, Customer1);
		comm1.addMember(Customer2);
		
		assertEquals(comm1.getName(),"Lottogruppe");
		assertEquals(comm1.getPassword(),"passt");
		assertTrue(comm1.isMember(Customer1));
	}

}
