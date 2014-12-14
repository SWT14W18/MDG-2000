package de.tudresden.swt14ws18.tips;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.gamemanagement.Game;

/**
 * Repräsentiert ein Tipp
 */
@MappedSuperclass
public abstract class Tip {

    @GeneratedValue
    @Id
    public long id;

    private boolean valid = true;

    public Tip() {
    }

    /**
     * Finde herraus ob der Tipp als valide betrachtet wird. Tipps können z.b. von ihrem Tippschein als invalide markiert werden, wenn die Beteiligten
     * ihren Anteil nicht zahlen können.
     * 
     * @return true wenn der Tipp valide ist, false wenn nicht
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Markiere den Tip als invalide, so dass dieser für die Tippabarbeitung ignoriert wird.
     */
    public void invalidate() {
        this.valid = false;
    }

    /**
     * Hole die Datenbank Id des Tips.
     * 
     * @return die Id des Tips
     */
    public long getId() {
        return id;
    }

    public abstract String toCustomString();

    /**
     * Hole das Spiel, zu welchem der Tipp gehört.
     * 
     * @return das Spiel, zu dem der Tipp gehört.
     */
    public abstract Game getGame();

    /**
     * Hole den Einsatz der mit diesem Tipp verbunden ist.
     * 
     * @return der Einsatz des Tipps. Bei Lotto immer 1, bei Toto variabel.
     */
    public abstract double getInput();

    /**
     * Hole wieviel Geld für diesen Tipp ausgeschüttet wird.
     * 
     * @return der Gewinnbetrag
     */
    public abstract double getWinAmount();

    /**
     * Finde herraus ob der Tip bereits abgearbeitet wurde oder nicht. Ist gleichzusetzen mit getGame().isFinished()
     * 
     * @return true wenn das Spiel fertig ist, false wenn nicht
     */
    public boolean isFinished() {
        return getGame().isFinished();
    }

}
