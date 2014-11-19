package de.tudresden.swt14ws18.useraccountmanager;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<ConcreteCustomer, Long> {
	
	ConcreteCustomer findByUserAccount(UserAccount userAccount);

}
