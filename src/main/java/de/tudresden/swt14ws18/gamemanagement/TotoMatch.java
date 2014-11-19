package de.tudresden.swt14ws18.gamemanagement;

import java.util.Map;
import java.util.Observable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class TotoMatch extends Observable {
    @GeneratedValue
    @Id
    private long id;

    private String teamHome;
    private String teamGuest;
    private Map<TotoResult, Double> quotes;
    private TotoResult result = TotoResult.NOT_PLAYED;

    public TotoMatch(String teamHome, String teamGuest, Map<TotoResult, Double> quotes) {
	this.teamGuest = teamGuest;
	this.teamHome = teamHome;
	this.quotes = quotes;
    }

    public String getTeamHome() {
	return teamHome;
    }

    public String getTeamGuest() {
	return teamGuest;
    }

    public TotoResult getResult() {
	return result;
    }

    public double getQuote(TotoResult result) {
	if (!quotes.containsKey(result))
	    return 1;

	return quotes.get(result);
    }

    public void setResult(TotoResult result) {
	if (result == TotoResult.NOT_PLAYED)
	    throw new IllegalArgumentException("You can't set the result of a game to NOT PLAYED!");

	if (getResult() != TotoResult.NOT_PLAYED)
	    throw new IllegalArgumentException("You can't set the result of a game, that already has been set!");

	this.result = result;
	this.notifyObservers(true); //report that the game is finished in all things
    }

    public long getId() {
	return id;
    }
}
