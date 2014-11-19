package de.tudresden.swt14ws18;

import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Component
public class LotterieDataInitializer implements DataInitializer{

	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;
	
	@Autowired
	public LotterieDataInitializer(CustomerRepository customerRepository, UserAccountManager userAccountManager){
		
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		
		this.customerRepository = customerRepository;
		this.userAccountManager = userAccountManager;
	}
	
	@Override
	public void initialize() {

		initializeUsers(userAccountManager, customerRepository);
	}
	
	private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository) {

		if (userAccountManager.get(new UserAccountIdentifier("boss")).isPresent()) {
			return;
		}

		UserAccount bossAccount = userAccountManager.create("boss", "123", new Role("ROLE_BOSS"));
		userAccountManager.save(bossAccount);

		final Role customerRole = new Role("ROLE_CUSTOMER");

		UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
		userAccountManager.save(ua1);
		UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
		userAccountManager.save(ua2);
		UserAccount ua3 = userAccountManager.create("earlhickey", "123", customerRole);
		userAccountManager.save(ua3);
		UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", customerRole);
		userAccountManager.save(ua4);

		ConcreteCustomer c1 = new ConcreteCustomer("hans","123",Status.ACTIVE, ua1);
		ConcreteCustomer c2 = new ConcreteCustomer("dextermorgan", "123", Status.ACTIVE, ua2);
		ConcreteCustomer c3 = new ConcreteCustomer("earlhickey", "123", Status.ACTIVE, ua3);
		ConcreteCustomer c4 = new ConcreteCustomer("mclovinfogell", "123", Status.ACTIVE, ua4);
		
		customerRepository.save(Arrays.asList(c1, c2, c3, c4));
	}
}

