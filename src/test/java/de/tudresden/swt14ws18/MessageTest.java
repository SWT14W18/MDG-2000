package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

public class MessageTest {

       

    private UserAccount userAccount = Lotterie.getInstance().getUserAccountManager()
            .create("Dieter", "678", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
    private BankAccount bankAccount = new BankAccount();


    @Test
    public void testMessage() {
        
        ConcreteCustomer Customer1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);


        Lotterie.getInstance().getUserAccountManager().save(userAccount);


        //Lotterie.getInstance().getCustomerRepository().save(Customer1);
        
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        Customer1.addMessage(GameType.LOTTO);
        
        //assertEquals(Customer1.getName(), "Dieter");
        
//        assertEquals(Customer1.getMessageCount(), 0);   




    }

}
