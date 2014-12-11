package de.tudresden.swt14ws18.modelTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.repositories.*;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

public class MessageTest {
   
    private final CustomerRepository customerRepository = Lotterie.getInstance().getCustomerRepository();
    private final MessageRepository messageRepo = Lotterie.getInstance().getMessagesRepository();
    private final UserAccountManager userAccountManager = Lotterie.getInstance().getUserAccountManager();
    private final BankAccountRepository bankAccountRepository = Lotterie.getInstance().getBankAccountRepository();


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
