package de.tudresden.swt14ws18.repositoryTests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;

/**
 * Test des BankAccountRepositories
 * 
 * Funktionen:
 * Account anlegen und speichern, Wiederfinden des eben gespeicherten Accounts, finden aller Accounts
 * 
 * @author Reinhard_2
 *
 */

public class BankAccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired BankAccountRepository bARepo;
    
    @Test
    public void findAllBankAccounts() {
        
        long size = bARepo.count();
        int sizeInt = new BigDecimal(size).intValueExact();
        
        
        assertThat(bARepo.findAll(), is(iterableWithSize(sizeInt)));
        
    }
    
    @Test
    public void addAnAccount(){
        BankAccount ba = new BankAccount();
        bARepo.save(ba);
        
        assertThat(bARepo.findAll(), hasItem(ba));
       
    }

}
