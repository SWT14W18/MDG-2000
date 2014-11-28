package de.tudresden.swt14ws18;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Component
public class LotterieDataInitializer implements DataInitializer {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoMatchRepository;
    private final LottoMatchRepository lottoMatchRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public LotterieDataInitializer(CustomerRepository customerRepository, UserAccountManager userAccountManager,
	    BankAccountRepository bankAccountRepository, TotoMatchRepository totoMatchRepository, LottoMatchRepository lottoMatchRepository,
	    MessageRepository messageRepository) {

	Assert.notNull(customerRepository, "CustomerRepository must not be null!");
	Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

	this.messageRepository = messageRepository;
	this.customerRepository = customerRepository;
	this.userAccountManager = userAccountManager;
	this.bankAccountRepository = bankAccountRepository;
	this.totoMatchRepository = totoMatchRepository;
	this.lottoMatchRepository = lottoMatchRepository;
    }

    @Override
    public void initialize() {
	initializeUsers(userAccountManager, customerRepository, bankAccountRepository);
	initializeData(lottoMatchRepository, totoMatchRepository);
    }
	
	private void initializeData(LottoMatchRepository lottoMatchRepository, TotoMatchRepository totoMatchRepository){
		

	for (int i = 0; i < 100; i++)
	    lottoMatchRepository.save(new LottoGame(Lotterie.getInstance().getTime().getTime()));
	
	if (totoMatchRepository.findAll().iterator().hasNext()) {
		return;
	}
	
	try {
	    new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.BUNDESLIGA1);
	    new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.BUNDESLIGA2);
//	    new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.POKAL);
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository,
	    BankAccountRepository bankAccountRepository) {

	// damit wir nicht immer den db ordner löschen müssen
	if (userAccountManager.get(new UserAccountIdentifier("admin")).isPresent()) {
	    return;
	}

	UserAccount admin = userAccountManager.create("admin", "123", new Role("ROLE_BOSS"));
	userAccountManager.save(admin);

	BankAccount adminAccount = new BankAccount();
	ConcreteCustomer adminCustomer = new ConcreteCustomer("admin", "123", Status.ACTIVE, admin, adminAccount);

	bankAccountRepository.save(adminAccount);
	customerRepository.save(adminCustomer);
	
	/**
	 * ein Dummy, der lediglich da ist, damit man das Einzahlen "schön" auf der Website ausgeben kann
	 */
	final Role customerRole = new Role("ROLE_USER");
	
	UserAccount dummyEinzahlenUserAccount = userAccountManager.create("einzahlung", "789", customerRole);
        userAccountManager.save(dummyEinzahlenUserAccount);
        BankAccount dummyEinzahlenAccount = new BankAccount();
        ConcreteCustomer dummyEinzahlenCustomer = new ConcreteCustomer("einzahlung", "789", Status.ACTIVE,dummyEinzahlenUserAccount, dummyEinzahlenAccount);
        
        bankAccountRepository.save(dummyEinzahlenAccount);
        customerRepository.save(dummyEinzahlenCustomer);
        
        /**
         * ein Dummy, der dasselbe für das Auszahlen tut .... hach, so viel Aufwand für die Schönheit ^^
         */
        
        UserAccount dummyAuszahlenUserAccount = userAccountManager.create("auszahlung", "789", customerRole);
        userAccountManager.save(dummyAuszahlenUserAccount);
        BankAccount dummyAuszahlenAccount = new BankAccount();
        ConcreteCustomer dummyAuszahlenCustomer = new ConcreteCustomer("auszahlung", "789", Status.ACTIVE,dummyEinzahlenUserAccount, dummyEinzahlenAccount);
        
        bankAccountRepository.save(dummyAuszahlenAccount);
        customerRepository.save(dummyAuszahlenCustomer);

	//final Role customerRole = new Role("ROLE_USER");

	UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
	userAccountManager.save(ua1);
	UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
	userAccountManager.save(ua2);
	UserAccount ua3 = userAccountManager.create("earlhickey", "123", customerRole);
	userAccountManager.save(ua3);
	UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", customerRole);
	userAccountManager.save(ua4);

	BankAccount ba1 = new BankAccount();
	BankAccount ba2 = new BankAccount();
	BankAccount ba3 = new BankAccount();
	BankAccount ba4 = new BankAccount();
	ba1.payIn(100);

	ConcreteCustomer c1 = new ConcreteCustomer("hans", "123", Status.ACTIVE, ua1, ba1);
	ConcreteCustomer c2 = new ConcreteCustomer("dextermorgan", "123", Status.ACTIVE, ua2, ba2);
	ConcreteCustomer c3 = new ConcreteCustomer("earlhickey", "123", Status.ACTIVE, ua3, ba3);
	ConcreteCustomer c4 = new ConcreteCustomer("mclovinfogell", "123", Status.ACTIVE, ua4, ba4);

	bankAccountRepository.save(Arrays.asList(ba1, ba2, ba3, ba4));
	customerRepository.save(Arrays.asList(c1, c2, c3, c4));
	
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.TOTO);
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.TOTO);
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.LOTTO);
	c1.addMessage(GameType.LOTTO);
	
	messageRepository.save(c1.getMessages());
	
    }
}
