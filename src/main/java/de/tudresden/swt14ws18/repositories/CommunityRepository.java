package de.tudresden.swt14ws18.repositories;

import java.util.List;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface CommunityRepository extends CrudRepository<Community, Long> {
	public Community findByUserAccount(UserAccount userAccount);
	
	public List<Community> findByMembersContains(ConcreteCustomer member);
}
