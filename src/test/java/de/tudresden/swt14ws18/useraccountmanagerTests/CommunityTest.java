package de.tudresden.swt14ws18.useraccountmanagerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Der CommunityTest umfasst das Testen der korrekten Gruppenfunktionen.
 * 
 * TODO Gruppentests vervollständigen, wenn Gruppenfunktionalität vollständig
 * 
 * @author Reinhard_2
 *
 */

public class CommunityTest extends AbstractIntegrationTest {
	
	@Autowired BankAccountRepository bRepo;
	@Autowired CustomerRepository cRepo;
	@Autowired UserAccountManager uaMan;
       

	@Test
	public void CreationTest() {
	        UserAccount ua1 = uaMan.create("Dieter", "678",Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
	        uaMan.save(ua1);
	        UserAccount ua2 = uaMan.create("Paul", "678",Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
	        uaMan.save(ua2);
	        
	        BankAccount ba1 = new BankAccount();
	        BankAccount ba2 = new BankAccount();
	        
	        bRepo.save(Arrays.asList(ba1,ba2));
	    
		ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter",Status.ACTIVE, ua1, ba1);
		ConcreteCustomer Customer2 = new ConcreteCustomer("Paul", Status.ANONYM, ua2, ba2);
		
		cRepo.save(Arrays.asList(Customer1, Customer2));
		
		Community comm1 = new Community("Lottogruppe","passt", Customer1);
		comm1.addMember(Customer2);
		
		assertEquals(comm1.getName(),"Lottogruppe");
		assertEquals(comm1.getPassword(),"passt");
		assertTrue(comm1.isMember(Customer1));
	}

}
