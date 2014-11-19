package de.tudresden.swt14ws18.useraccountmanager;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface CommunityRepository extends CrudRepository<Community, Long> {
	public Community findByUserAccount(UserAccount userAccount);
}
