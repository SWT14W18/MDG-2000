package de.tudresden.swt14ws18.useraccountmanagerTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Der MessageTest überprüft die korrekte Funktionalität des Mitteilungssystems der Lotterie. 
 * 
 * @author Reinhard_2
 *
 */

public class MessageTest extends AbstractIntegrationTest{
   
    @Autowired CustomerRepository customerRepository;
    @Autowired MessageRepository messageRepo;
    @Autowired UserAccountManager userAccountManager;
    @Autowired BankAccountRepository bankAccountRepository;


    @Test
    public void testMessage() {
        
        UserAccount userAccount = userAccountManager
                .create("Dieter", "678", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(userAccount);
        BankAccount bankAccount = new BankAccount();
        bankAccountRepository.save(bankAccount);
        
        ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);
        customerRepository.save(Customer1);
        
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        
        //assertEquals(Customer1.getName(), "Dieter");
        
       assertEquals(Customer1.getMessageCount(), 5);   




    }

}
