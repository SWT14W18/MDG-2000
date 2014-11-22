package de.tudresden.swt14ws18.gamemanagement;

import java.util.Date;
import java.util.Observable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.tips.Finishable;

@MappedSuperclass
public abstract class Game extends Observable implements Finishable {
    @GeneratedValue
    @Id
    private long id;
    private static long idCounter = 0;

    private Date date;
    
    @Deprecated
    protected Game(){
    }

    public Game(Date date) {
	this.date = date;
	id = idCounter++;
    }

    public long getId() {
	return id;
    }

    public abstract String getTitle();

    public abstract GameType getType();

    public Date getDate() {
	return date;
    }

    public abstract boolean isFinished();
}