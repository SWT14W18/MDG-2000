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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Lotterie.class)
@ContextConfiguration(classes = Lotterie.class)//{CustomerRepository.class, UserAccountManager.class, BankAccountRepository.class, BankAccount.class, ConcreteCustomer.class})
//@ContextConfiguration(classes = Lotterie.class)//locations ={"/MDG-2000/pom.xml"})
//@Transactional
public class AbstractIntegrationTest {

}
