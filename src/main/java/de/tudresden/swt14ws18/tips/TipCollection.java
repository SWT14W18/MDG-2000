package de.tudresden.swt14ws18.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Customer;

public class TipCollection implements Observer {

    private Lotterie lotterie;

    private TipShare shares = new TipShare();
    private ConcreteCustomer owner;
    private List<Tip> tips = new ArrayList<>();

    public TipCollection(List<Tip> tips, ConcreteCustomer owner) {
	this.tips = tips;
	this.owner = owner;
	for (Tip tip : tips)
	    tip.addObserver(this);
    }

    @Autowired
    protected void setLotterie(Lotterie lotterie) {
	this.lotterie = lotterie;
    }

    public Lotterie getLotterie() {
	return lotterie;
    }

    public List<Tip> getTips() {
	return tips;
    }

    public Customer getOwner() {
	return owner;
    }

    public TipShare getShares() {
	return shares;
    }

    @Override
    public void update(Observable o, Object arg) {
	if ((Boolean) arg)
	    checkMoney(o);
	else
	    payout(o);
    }

    private void payout(Observable o) {
	Tip tip = (Tip) o;

	if (!tip.isValid())
	    return;

	double win = tip.getWinAmount();
	double ownerExtra = getShares().getShare(owner);

	BankAccount lotterie = getLotterie().getBankAccount();
	if(ownerExtra + getShares().getRemainingShare() > 0)
	    lotterie.outgoingTransaction(owner.getAccount(), win * (ownerExtra + getShares().getRemainingShare()));
	
	for (Entry<ConcreteCustomer, Double> entry : getShares())
	{
	    ConcreteCustomer customer = entry.getKey();
	    Double value = entry.getValue();

	    if (customer == owner)
		continue;
	    
	    lotterie.outgoingTransaction(customer.getAccount(), win * value);
	}
    }

    private void checkMoney(Observable o) {
	Tip tip = (Tip) o;

	double ownerExtra = getShares().getShare(owner);

	// check if money is available
	for (Entry<ConcreteCustomer, Double> entry : getShares()) {
	    ConcreteCustomer customer = entry.getKey();
	    Double value = entry.getValue();

	    if (customer == owner) 
		continue;

	    if (!customer.getAccount().hasBalance(value * tip.getInput())) {
		customer.addMessage();
		tip.invalidate(false);
	    }
	}

	if (owner.getAccount().hasBalance(tip.getInput() * (getShares().getRemainingShare() + ownerExtra))) {
	    owner.addMessage();
	    tip.invalidate(false);
	}

	// take money
	for (Entry<ConcreteCustomer, Double> entry : getShares()) {
	    ConcreteCustomer customer = entry.getKey();
	    Double value = entry.getValue();

	    if (customer == owner)
		continue;

	    customer.getAccount().outgoingTransaction(getLotterie().getBankAccount(), tip.getInput() * value);
	}

	owner.getAccount().outgoingTransaction(getLotterie().getBankAccount(), tip.getInput() * (getShares().getRemainingShare() + ownerExtra));
    }
}
