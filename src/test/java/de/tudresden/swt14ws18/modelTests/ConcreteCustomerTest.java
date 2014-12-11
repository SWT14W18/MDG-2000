package de.tudresden.swt14ws18.modelTests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Message;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

public class ConcreteCustomerTest extends AbstractIntegrationTest{

    
        @Autowired UserAccountManager uAManager;
        @Autowired CustomerRepository customerRepo;
        @Autowired BankAccountRepository bARepo;
        @Autowired MessageRepository msgRepo;
    
	
	@SuppressWarnings("deprecation")
        @Test
	public void testConcreteCustomer() {
	        UserAccount userAccount = uAManager.create("Dieter","345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
	        uAManager.save(userAccount);
	        
	        BankAccount bankAccount = new BankAccount();
	        
	        ConcreteCustomer c1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);
	        
	        bARepo.save(bankAccount);
	        customerRepo.save(c1);
	        
	        assertEquals(c1.getName(), "Dieter");
	        assertEquals(c1.getMessageCount(), 0);	        
	        assertEquals(c1.getState(), Status.ACTIVE);
	        
	        c1.addMessage(GameType.LOTTO);
	        assertThat(c1.getMessages(), is(iterableWithSize(c1.getMessageCount())));
	        
	        c1.getAccount().incomingTransaction(new Transaction(null, c1.getAccount(), 200.0));
	        
	        assertEquals(c1.getAccount().getBalance(), 200.0, 0.01);
	      
	        Iterable<Message> messages = c1.getMessages();
	        
	        while(messages.iterator().hasNext()){
	            c1.payOneMessage(messages.iterator().next());
	        }
	        
	        
	        assertEquals(c1.getAccount().getBalance(), 198.0, 0.01);
	        
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        c1.addMessage(GameType.LOTTO);
	        
	        assertEquals(c1.getState(), Status.BLOCKED);
	        	        
	        while(messages.iterator().hasNext()){
                    c1.payOneMessage(messages.iterator().next());
                }	        
	        assertEquals(c1.getState(), Status.ACTIVE);
	       
	}

}