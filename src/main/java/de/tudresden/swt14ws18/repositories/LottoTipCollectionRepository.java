package de.tudresden.swt14ws18.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.tips.LottoTipCollection;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface LottoTipCollectionRepository extends CrudRepository<LottoTipCollection, Long> {

    public Collection<LottoTipCollection> findByOwner(ConcreteCustomer customer);

}
