package de.tudresden.swt14ws18;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Message;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;
import de.tudresden.swt14ws18.util.Util;

public class MessageTest {
    
    private UserAccount userAccount = Lotterie.getInstance().getUserAccountManager().create("Dieter", "678", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
    private BankAccount bankAccount = new BankAccount();
    
    @Test
    public void testMessage() {
        
        Lotterie.getInstance().getUserAccountManager().save(userAccount);
        
        ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);
        Lotterie.getInstance().getCustomerRepository().save(Customer1);
        
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        
        assertEquals(Customer1.getName(), "Dieter");
        
       // assertEquals(Customer1.getMessageCount(), 5);   
  
    }

}
