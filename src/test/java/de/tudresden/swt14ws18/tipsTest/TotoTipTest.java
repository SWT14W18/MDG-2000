package de.tudresden.swt14ws18.tipsTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Dieser Test befasst sich mit der Prüfung der TotoTipp-Erstellung.
 * 
 * Funktionen: Ein Kunde (Kundentest im ConcreteCustomerTest) erstellt einen Tipp über die TotoTippFactory und überprüft, ob der Tipp erstellt wurde
 * 
 * @author Reinhard_2
 *
 */

public class TotoTipTest extends AbstractIntegrationTest {
    @Autowired
    TipFactory tipFactory;
    @Autowired
    UserAccountManager uAManager;
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    BankAccountRepository bARepo;
    @Autowired
    TotoMatchRepository totoMatchRepository;

    @Test
    public void craftATotoTip() {
        TotoMatch match = new TotoMatch("FC Test", "SG Beispiel", null, LocalDateTime.now(), TotoGameType.BUNDESLIGA1, 1, 1);
        totoMatchRepository.save(match);

        UserAccount userAccount = uAManager.create("Dieter", "345", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        uAManager.save(userAccount);
        BankAccount bankAccount = new BankAccount();
        bankAccount.payIn(200.0);
        ConcreteCustomer c1 = new ConcreteCustomer("Dieter", Status.ACTIVE, userAccount, bankAccount);

        bARepo.save(bankAccount);
        customerRepo.save(c1);

        Map<String, String> map = new HashMap<>();

        assertFalse(tipFactory.craftTotoTips(map, null));

        map.put(String.valueOf(match.getId()), "asd");

        assertFalse(tipFactory.craftTotoTips(map, null));

        map.put(String.valueOf(match.getId()), "1");
        map.put("input" + String.valueOf(match.getId()), "asd");

        assertFalse(tipFactory.craftTotoTips(map, null));

        map.put("input" + String.valueOf(match.getId()), "1");

        assertTrue(tipFactory.craftTotoTips(map, null));

    }
}
