package de.tudresden.swt14ws18.tipmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class Game {
    @GeneratedValue
    @Id
    private long id;

    private List<Observer> observer = new ArrayList<>();
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

    public void register(Observer obs) {
	observer.add(obs);
    }
}
