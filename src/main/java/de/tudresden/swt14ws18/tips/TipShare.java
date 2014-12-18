package de.tudresden.swt14ws18.tips;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

/**
 * Verwaltet die Anteile die Kunden an einem Tippschein haben können.
 */
public class TipShare implements Iterable<Entry<Long, Double>>, Serializable {

    private static final long serialVersionUID = -4805968579498236694L;
    private Map<Long, Double> share = new HashMap<>();
    private double remainingShare = 1;

    public TipShare() {
    }

    /**
     * Hole den noch nicht zugewiesenen Anteil an dem Tipp. Dieser Anteil wird bei der Tippauswertung immer dem Ersteller des Tipps zugewiesen.
     * 
     * @return der verbleibende Anteil
     */
    public double getRemainingShare() {
        return remainingShare;
    }

    /**
     * Finde herraus, ob der Kunde bereits ein Anteil an dem Tipp hat.
     * 
     * @param customer
     *            der Kunde, der überprüft werden soll.
     * @return true wenn der Kunde ein Anteil hat, false wenn nicht.
     */
    public boolean containsShareholder(ConcreteCustomer customer) {
        return share.containsKey(customer.getId());
    }

    /**
     * Hole den Anteil, den der Kunde an dem Tipp hält.
     * 
     * @param customer
     *            der Kunde
     * @return der Anteil als double, 0 falls der Kunde keinen Anteil hält
     */
    public double getShare(ConcreteCustomer customer) {
        return share.containsKey(customer.getId()) ? share.get(customer.getId()) : 0;
    }

    /**
     * Füge einen Kundenanteil hinzu. Es kann nur soviel Anteil vergeben werden, wie noch frei ist.
     * 
     * @param customer
     *            der Kunde
     * @param amount
     *            der Anteil
     * @return true wenn der Anteil erfolgreich hinzugefügt wurde, false wenn nicht.
     */
    public boolean addShareholder(ConcreteCustomer customer, double amount) {
        if (share.containsKey(customer.getId()))
            removeShareholder(customer);

        if (amount > remainingShare)
            return false;

        share.put(customer.getId(), amount);
        remainingShare -= amount;

        return true;
    }

    /**
     * Entferne einen Kundenanteil und fügr dessen Prozente den "remainingShare" hinzu.
     * 
     * @param customer
     *            der Kunde dessen Anteil entfernt werden soll
     * @return true wenn etwas verändert wurde, false wenn nicht
     */
    public boolean removeShareholder(ConcreteCustomer customer) {
        if (!share.containsKey(customer.getId()))
            return false;

        remainingShare += share.get(customer.getId());
        share.remove(customer.getId());
        return true;
    }

    @Override
    public Iterator<Entry<Long, Double>> iterator() {
        return share.entrySet().iterator();
    }
}
