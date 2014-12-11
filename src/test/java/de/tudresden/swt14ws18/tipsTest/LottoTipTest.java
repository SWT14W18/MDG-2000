package de.tudresden.swt14ws18.tipsTest;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.LottoTipCollection;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * 
 * Dieser Test umfasst den Test der LottoTipCollection, des LottoTipRepositories
 * 
 * Gleichzeitig prüft dieser Test, ob die TippCollection auch für Gemeinschaften funktioniert
 * 
 * @author Reinhard_2
 *
 */


public class LottoTipTest extends AbstractIntegrationTest{
    
    @Autowired LottoTipCollectionRepository ltcRepo;
    @Autowired UserAccountManager uAManager;
    @Autowired CustomerRepository customerRepo;
    @Autowired BankAccountRepository bARepo;
    @Autowired CommunityRepository commRepo;

    @Test
    public void createALottoTipTest(){
        
        LottoGame game = new LottoGame(Lotterie.getInstance().getTime().getTime());        
        LottoTip tip = new LottoTip(game, new LottoNumbers(8, 2, 9, 1, 7, 6, 8));
        
        UserAccount userAccount = uAManager.create("Dieter","345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        uAManager.save(userAccount);       
        BankAccount bankAccount = new BankAccount();
        bankAccount.payIn(200.0);
        ConcreteCustomer c1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);
        
        bARepo.save(bankAccount);
        customerRepo.save(c1);
        
        
        LottoTipCollection tipps = new LottoTipCollection(Arrays.asList(tip), c1, null);
        
        ltcRepo.save(tipps);
        
        assertThat(ltcRepo.findAll(), hasItem(tipps));
        assertThat(tipps.getTips(), hasItem(tip));
        
        
        
    }
    
    @Test
    public void createALottoTipCommunity(){
        
        LottoGame game = new LottoGame(Lotterie.getInstance().getTime().getTime());        
        LottoTip tip = new LottoTip(game, new LottoNumbers(8, 2, 9, 1, 7, 6, 8));
        
        UserAccount userAccount = uAManager.create("peter","345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        uAManager.save(userAccount);
        UserAccount userAccount2 = uAManager.create("siegbert","345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        uAManager.save(userAccount);
        
        BankAccount bankAccount = new BankAccount();
        BankAccount bankAccount2 = new BankAccount();
        
        bankAccount.payIn(200.0);
        bankAccount2.payIn(200.0);
        
        ConcreteCustomer c1 = new ConcreteCustomer("peter", Status.ACTIVE, userAccount, bankAccount);
        ConcreteCustomer c2 = new ConcreteCustomer("peter", Status.ACTIVE, userAccount2, bankAccount2);
        
        bARepo.save(bankAccount);
        bARepo.save(bankAccount2);
        customerRepo.save(Arrays.asList(c1,c2));
        
        Community comm1 = new Community("Gewinngemeinschaft", "345", c1);
        commRepo.save(comm1);
        
        comm1.addMember(c2);
        
        assertThat(comm1.getAdmin(), is(c1));
        assertEquals(comm1.getName(), "Gewinngemeinschaft");
        
        LottoTipCollection tipps = new LottoTipCollection(Arrays.asList(tip), c1, comm1);
        
    }

}
