package de.tudresden.swt14ws18.tips;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

public class TipShare implements Iterable<Entry<ConcreteCustomer, Double>>{
    Map<ConcreteCustomer, Double> share = new HashMap<>();
    double remainingShare = 1;

    public TipShare() {

    }

    public double getRemainingShare() {
	return remainingShare;
    }

    public boolean addShareholder(ConcreteCustomer customer, double amount) {
	if (share.containsKey(customer))
	    removeShareholder(customer);

	if (amount > remainingShare)
	    return false;
	
	share.put(customer, amount);
	remainingShare -= amount;

	return true;
    }

    public boolean removeShareholder(ConcreteCustomer customer) {
	if(!share.containsKey(customer))
	    return false;
	
	remainingShare += share.get(customer);
	share.remove(customer);
	return true;
    }

    @Override
    public Iterator<Entry<ConcreteCustomer, Double>> iterator() {
	return share.entrySet().iterator();
    }
}
