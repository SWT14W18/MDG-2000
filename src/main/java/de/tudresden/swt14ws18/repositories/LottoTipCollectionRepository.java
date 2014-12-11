package de.tudresden.swt14ws18.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.LottoTipCollection;
import de.tudresden.swt14ws18.tips.TotoTipCollection;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public interface LottoTipCollectionRepository extends CrudRepository<LottoTipCollection, Long> {

    public Collection<LottoTipCollection> findByOwner(ConcreteCustomer customer);
    public LottoTipCollection findByTips(LottoTip tip);
    public List<LottoTipCollection> findByCommunity (Community community);

}
