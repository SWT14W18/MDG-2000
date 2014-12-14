package de.tudresden.swt14ws18.gamemanagement;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.util.Constants;

/**
 * Repräsentiert ein Spiel
 */
@MappedSuperclass
public abstract class Game {
    @GeneratedValue
    @Id
    private long id;

    private LocalDateTime date;

    @Deprecated
    protected Game() {
    }

    public Game(LocalDateTime date) {
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
    public LocalDateTime getDate() {
        return date;
    }

    public String getDateString() {
        return date.format(Constants.OUTPUT_DTF);
    }

    /**
     * Finde herraus ob das Spiel bereits abgeschlossen ist oder nicht.
     * 
     * @return true wenn das Spiel abgeschlossen ist, false wenn nicht.
     */
    public abstract boolean isFinished();
}
