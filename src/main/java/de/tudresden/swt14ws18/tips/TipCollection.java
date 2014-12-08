package de.tudresden.swt14ws18.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@MappedSuperclass
public abstract class TipCollection<T extends Tip> {

    @GeneratedValue
    @Id
    private long id;

    private TipShare shares = new TipShare();

    @ManyToOne
    private ConcreteCustomer owner;

    @ElementCollection
    private List<T> tips = new ArrayList<>();

    @Deprecated
    public TipCollection() {

    }

    public long getId() {
        return id;
    }

    public TipCollection(List<T> tips, ConcreteCustomer owner) {
        this.tips = tips;
        this.owner = owner;
    }

    public List<T> getTips() {
        return tips;
    }

    public ConcreteCustomer getOwner() {
        return owner;
    }

    public TipShare getShares() {
        return shares;
    }

    public void update(Tip o, Object arg) {
        if ((Boolean) arg)
            checkMoney(o);
        else
            payout(o);
    }

    public boolean isFinished() {
        for (Tip tip : tips)
            if (!tip.isFinished())
                return false;

        return true;
    }

    private void payout(Tip o) {
        Tip tip = (Tip) o;

        if (!tip.isValid())
            return;

        double win = tip.getWinAmount();
        double ownerExtra = getShares().getShare(owner);

        BankAccount lotterie = Lotterie.getInstance().getBankAccount();
        if (ownerExtra + getShares().getRemainingShare() > 0)
            lotterie.outgoingTransaction(owner.getAccount(), win * (ownerExtra + getShares().getRemainingShare()));

        for (Entry<ConcreteCustomer, Double> entry : getShares()) {
            ConcreteCustomer customer = entry.getKey();
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            lotterie.outgoingTransaction(customer.getAccount(), win * value);
        }
    }

    private void checkMoney(Tip o) {
        Tip tip = (Tip) o;

        double ownerExtra = getShares().getShare(owner);

        // check if money is available
        for (Entry<ConcreteCustomer, Double> entry : getShares()) {
            ConcreteCustomer customer = entry.getKey();
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            if (!customer.getAccount().hasBalance(value * tip.getInput())) {
                customer.addMessage(getGameType());
                tip.invalidate(false);
            }
        }

        if (!owner.getAccount().hasBalance(tip.getInput() * (getShares().getRemainingShare() + ownerExtra))) {
            owner.addMessage(getGameType());
            tip.invalidate(false);
        }

        if (!tip.isValid())
            return;

        // take money
        for (Entry<ConcreteCustomer, Double> entry : getShares()) {
            ConcreteCustomer customer = entry.getKey();
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            customer.getAccount().outgoingTransaction(Lotterie.getInstance().getBankAccount(), tip.getInput() * value);
        }

        owner.getAccount().outgoingTransaction(Lotterie.getInstance().getBankAccount(),
                tip.getInput() * (getShares().getRemainingShare() + ownerExtra));
    }

    public abstract GameType getGameType();
}
