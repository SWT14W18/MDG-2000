package de.tudresden.swt14ws18.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

/**
 * Repräsentiert einen Tippschein
 * 
 * @param <T>
 *            der Type von Tipps, die dieser Tippschein halten kann.
 */
@MappedSuperclass
public abstract class TipCollection<T extends Tip> {

    @GeneratedValue
    @Id
    private long id;

    @Column(length = 2048)
    private TipShare shares;

    @ManyToOne
    private Community community;

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

    public TipCollection(List<T> tips, ConcreteCustomer owner, Community community) {
        this.community = community;
        this.tips = tips;
        this.owner = owner;
        this.shares = new TipShare();
    }

    public TipCollection(List<T> tips, ConcreteCustomer owner) {
        this(tips, owner, null);
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
    
    public Community getCommunity() {
        return community;
    }
    
    public boolean hasCommunity() {
        return community != null;
    }

    /**
     * Überreste des Observer Patterns
     * 
     * Meldet dem Tippschein, dass einer seiner Tipps fertig ist. Es gibt 2 Modies. Modus "true" überprüft ob der Tipp valide ist und zieht
     * gegebenenfalls das Geld ab. Modus "false" überweist den Spielern ihren Gewinn
     * 
     * @param o der Tip, der geupdated wurde
     * @param arg true wenn das Geld überprüft werden soll, false wenn das Ergebnis ausgezahlt werden soll
     */
    public void update(Tip o, boolean arg) {
        if (arg)
            checkMoney(o);
        else
            payout(o);
    }

    /**
     * Finde herraus ob der Tippschein komplett fertig ist, d.h. ob alle Tipps fertig sind.
     * 
     * @return true wenn alle Tipps fertig sind, false wenn nicht
     */
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
        
        if(win <= 0)
            return;
        
        double ownerExtra = getShares().getShare(owner);

        BankAccount lotterie = Lotterie.getInstance().getBankAccount();
        if (ownerExtra + getShares().getRemainingShare() > 0)
            lotterie.outgoingTransaction(owner.getAccount(), win * (ownerExtra + getShares().getRemainingShare()), getGameType().name() + " Gewinn");

        for (Entry<Long, Double> entry : getShares()) {
            ConcreteCustomer customer = Lotterie.getInstance().getCustomerRepository().findOne(entry.getKey());
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            lotterie.outgoingTransaction(customer.getAccount(), win * value, getGameType().name() + " Gewinn");
        }
    }

    private void checkMoney(Tip o) {
        Tip tip = (Tip) o;

        double ownerExtra = getShares().getShare(owner);

        // check if money is available
        for (Entry<Long, Double> entry : getShares()) {
            ConcreteCustomer customer = Lotterie.getInstance().getCustomerRepository().findOne(entry.getKey());
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            if (!customer.getAccount().hasBalance(value * tip.getInput())) {
                customer.addMessage(getGameType());
                tip.invalidate();
            }
        }

        if (!owner.getAccount().hasBalance(tip.getInput() * (getShares().getRemainingShare() + ownerExtra))) {
            owner.addMessage(getGameType());
            tip.invalidate();
        }

        if (!tip.isValid())
            return;

        // take money
        for (Entry<Long, Double> entry : getShares()) {
            ConcreteCustomer customer = Lotterie.getInstance().getCustomerRepository().findOne(entry.getKey());
            Double value = entry.getValue();

            if (customer == owner)
                continue;

            customer.getAccount().outgoingTransaction(Lotterie.getInstance().getBankAccount(), tip.getInput() * value,
                    getGameType().name() + " Einsatz");
        }

        owner.getAccount().outgoingTransaction(Lotterie.getInstance().getBankAccount(),
                tip.getInput() * (getShares().getRemainingShare() + ownerExtra), getGameType().name() + " Einsatz");
    }

    /**
     * Entferne einen Tipp vom Tippschein
     * 
     * @param tip
     *            der zu entfernende Tipp. Wenn der Tipp nicht zu dem Tippschein gehört, passiert nichts.
     */
    public void removeTip(Tip tip) {
        tips.remove(tip);
    }

    /**
     * Hole den Type des Tippscheins.
     * 
     * @return der Type des Tippscheins.
     */
    public abstract GameType getGameType();
}
