package de.tudresden.swt14ws18.gamemanagement;

import java.util.Date;
import java.util.Observable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Repräsentiert ein Spiel
 */
@MappedSuperclass
public abstract class Game extends Observable {
    @GeneratedValue
    @Id
    private long id;

    private Date date;

    @Deprecated
    protected Game() {
    }

    public Game(Date date) {
	this.date = date;
    }

    /**
     * Hole die ID dieses Spiels
     * 
     * @return die ID des Spiels
     */
    public long getId() {
	return id;
    }

    /**
     * Hole den Title des Spiels, welche in der Spiel übersicht angezeigt wird.
     * 
     * @return den Title als String
     */
    public abstract String getTitle();

    /**
     * Hole den Type des Spiels
     * 
     * @return der GameType des Spiels
     */
    public abstract GameType getType();

    /**
     * Hole das Datum an welchem das Spiel stattfindet.
     * 
     * @return das Datum als Date.
     */
    public Date getDate() {
	return date;
    }

    /**
     * Finde herraus ob das Spiel bereits abgeschlossen ist oder nicht.
     * 
     * @return true wenn das Spiel abgeschlossen ist, false wenn nicht.
     */
    public abstract boolean isFinished();
}
