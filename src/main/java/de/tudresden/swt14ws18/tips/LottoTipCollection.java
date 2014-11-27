package de.tudresden.swt14ws18.tips;

import java.util.List;

import javax.persistence.Entity;

import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Entity
public class LottoTipCollection extends TipCollection<LottoTip> {

    @Deprecated
    public LottoTipCollection() {
	
    }
    
    public LottoTipCollection(List<LottoTip> tips, ConcreteCustomer owner) {
	super(tips, owner);
    }

}
