package de.tudresden.swt14ws18.gamemanagement;

import java.util.Observable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class TotoMatch extends Observable {
    @GeneratedValue
    @Id
    private long id;

    private String teamHome;
    private String teamGuest;
    private Result result = Result.NOT_PLAYED;

    public TotoMatch(String teamHome, String teamGuest) {
	this.teamGuest = teamGuest;
	this.teamHome = teamHome;
    }

    public String getTeamHome() {
	return teamHome;
    }

    public String getTeamGuest() {
	return teamGuest;
    }

    public Result getResult() {
	return result;
    }

    public void setResult(Result result) {
	this.result = result;
	this.notifyObservers();
    }

    public long getId() {
	return id;
    }
}
