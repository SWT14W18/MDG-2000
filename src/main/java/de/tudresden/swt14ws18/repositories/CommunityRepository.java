package de.tudresden.swt14ws18.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface CommunityRepository extends CrudRepository<Community, Long> {
    public List<Community> findByMembers(ConcreteCustomer member);

    public List<Community> findByPassword(String password);

    public Community findById(long parseLong);
}
