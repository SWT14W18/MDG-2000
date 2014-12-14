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
public class TipShare implements Iterable<Entry<ConcreteCustomer, Double>>, Serializable {

    private static final long serialVersionUID = -4805968579498236694L;
    private Map<ConcreteCustomer, Double> share = new HashMap<>();
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
        return share.containsKey(customer);
    }

    /**
     * Hole den Anteil, den der Kunde an dem Tipp hält.
     * 
     * @param customer
     *            der Kunde
     * @return der Anteil als double, 0 falls der Kunde keinen Anteil hält
     */
    public double getShare(ConcreteCustomer customer) {
        return share.containsKey(customer) ? share.get(customer) : 0;
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
        if (share.containsKey(customer))
            removeShareholder(customer);

        if (amount > remainingShare)
            return false;

        share.put(customer, amount);
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
        if (!share.containsKey(customer))
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
