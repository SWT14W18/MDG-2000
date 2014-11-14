package lotterie;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class LotterieDataInitializer implements DataInitializer {

	private final UserAccountManager userAccountManager;
	//private final CustomerRepository customerRepository;
	
	@Autowired
	public LotterieDataInitializer(UserAccountManager userAccountManager){
		
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		
		this.userAccountManager = userAccountManager;
	}
	
	@Override
	public void initialize() {
		initializeUsers();
	}
	
	private void initializeUsers(){
		
		final Role customerRole = new Role("ROLE_CUSTOMER");
		
		UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
		userAccountManager.save(ua1);
		UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
		userAccountManager.save(ua2);
		
	}

}
