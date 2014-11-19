package de.tudresden.swt14ws18;

import java.util.Arrays;
import java.util.Date;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.BankAccountRepository;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Component
public class LotterieDataInitializer implements DataInitializer{

	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;
	private final BankAccountRepository bankAccountRepository;
	
	@Autowired
	public LotterieDataInitializer(CustomerRepository customerRepository, UserAccountManager userAccountManager, BankAccountRepository bankAccountRepository){
		
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		
		this.customerRepository = customerRepository;
		this.userAccountManager = userAccountManager;
		this.bankAccountRepository = bankAccountRepository;
	}
	
	@Override
	public void initialize() {

		initializeUsers(userAccountManager, customerRepository, bankAccountRepository);
		
		Lotterie.getInstance().getGameManager().addGame(new LottoGame(new Date()));
	}
	
	private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository, BankAccountRepository bankAccountRepository) {

//		if (userAccountManager.get(new UserAccountIdentifier("boss")).isPresent()) {
//			return;
//		}
//
//		UserAccount bossAccount = userAccountManager.create("boss", "123", new Role("ROLE_BOSS"));
//		userAccountManager.save(bossAccount);

		final Role customerRole = new Role("ROLE_USER");

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
		
		bankAccountRepository.save(Arrays.asList(ba1,ba2,ba3,ba4));

		ConcreteCustomer c1 = new ConcreteCustomer("hans","123",Status.ACTIVE, ua1,ba1);
		ConcreteCustomer c2 = new ConcreteCustomer("dextermorgan", "123", Status.ACTIVE, ua2,ba2);
		ConcreteCustomer c3 = new ConcreteCustomer("earlhickey", "123", Status.ACTIVE, ua3,ba3);
		ConcreteCustomer c4 = new ConcreteCustomer("mclovinfogell", "123", Status.ACTIVE, ua4,ba4);
		
		customerRepository.save(Arrays.asList(c1, c2, c3, c4));
	}
}

