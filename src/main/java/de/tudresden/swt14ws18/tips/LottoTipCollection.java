package de.tudresden.swt14ws18.tips;

import java.util.List;

import javax.persistence.Entity;

import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

/**
 * Repräsentiert ein Lotto Tippschein
 */
@Entity
public class LottoTipCollection extends TipCollection<LottoTip> {

    @Deprecated
    public LottoTipCollection() {

    }

    public LottoTipCollection(List<LottoTip> tips, ConcreteCustomer owner, Community community) {
        super(tips, owner, community);
    }

    @Override
    public GameType getGameType() {
        return GameType.LOTTO;
    }
}
