package de.tudresden.swt14ws18.tips;

import java.util.List;

import javax.persistence.Entity;

import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Entity
public class TotoTipCollection extends TipCollection<TotoTip> {
    
    @Deprecated
    public TotoTipCollection() {
	
    }
    
    public TotoTipCollection(List<TotoTip> tips, ConcreteCustomer owner, Community community) {
	super(tips, owner, community);
    }

    @Override
    public GameType getGameType(){
        return GameType.TOTO;
    }
}
