package de.tudresden.swt14ws18.repositories;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.useraccountmanager.Community;

public interface CommunityRepository extends CrudRepository<Community, Long> {
	public Community findByUserAccount(UserAccount userAccount);
}
