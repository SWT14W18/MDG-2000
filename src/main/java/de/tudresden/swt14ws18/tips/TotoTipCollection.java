package de.tudresden.swt14ws18.tips;

import java.util.List;

import javax.persistence.Entity;

import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Entity
public class TotoTipCollection extends TipCollection<TotoTip> {

    public TotoTipCollection(List<TotoTip> tips, ConcreteCustomer owner) {
	super(tips, owner);
    }

}