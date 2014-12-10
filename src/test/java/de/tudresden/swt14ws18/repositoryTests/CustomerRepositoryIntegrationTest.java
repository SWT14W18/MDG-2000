package de.tudresden.swt14ws18.repositoryTests;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.LotterieDataInitializer;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;


public class CustomerRepositoryIntegrationTest extends AbstractIntegrationTest{

    @Autowired CustomerRepository customers;
    @Autowired UserAccountManager uAManager;
    @Autowired BankAccountRepository bARepo;
  
    
    
    
    @Test
    public void findAllCustomers() {
        
        UserAccount ua1 = uAManager.create("Testperson", "345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        uAManager.save(ua1);
        
        BankAccount ba1 = new BankAccount();       
        
        ConcreteCustomer c1 = new ConcreteCustomer("Testperson", Status.ACTIVE, ua1, ba1);
        
        bARepo.save(ba1);
        customers.save(c1);
        
        long size = customers.count();
        int sizeInt = new BigDecimal(size).intValueExact();
        
        Iterable<ConcreteCustomer> result = customers.findAll();
        
        assertThat(customers.findAll(), hasItem(c1));
        
        assertThat(result, is(iterableWithSize(sizeInt)));
       
    }

}
