package de.tudresden.swt14ws18.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.tips.TotoTip;
import de.tudresden.swt14ws18.tips.TotoTipCollection;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface TotoTipCollectionRepository extends CrudRepository<TotoTipCollection, Long> {

    public Collection<TotoTipCollection> findByOwner(ConcreteCustomer customer);

    public TotoTipCollection findByTips(TotoTip totoTip);
}
