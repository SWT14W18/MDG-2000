package de.tudresden.swt14ws18;


import org.junit.runner.RunWith;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

/**
 * Diese Klasse dient als Grundgerüst aller Tests, die in unserem Projekt an den Models vollzogen werden
 * NICHT AN CONTROLLERN!
 * 
 * Hier ist die Startkonfiguration geregelt und der TestContext definiert
 * 
 * Von dieser Klasse erbene alle weiteren Tests!
 * 
 * @author Reinhard_2
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Lotterie.class)
@ContextConfiguration(classes = Lotterie.class) //locations ={"/MDG-2000/pom.xml"})
@Transactional
public class AbstractIntegrationTest {

}
