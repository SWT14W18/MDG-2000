package de.tudresden.swt14ws18.gamemanagement;

import java.util.Date;
import java.util.Observable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class Game extends Observable {
    @GeneratedValue
    @Id
    private long id;

    private Date date;

    public Game(Date date) {
	this.date = date;
    }

    public long getId() {
	return id;
    }

    public abstract String getTitle();

    public abstract GameType getType();

    public Date getDate() {
	return date;
    }
}