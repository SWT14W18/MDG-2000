package de.tudresden.swt14ws18.gamemanagement;

import java.util.Date;
import java.util.Map;
import java.util.Observable;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import de.tudresden.swt14ws18.tips.Finishable;

@Entity
public class TotoMatch extends Game{

    private String teamHome; 
    private String teamGuest;
    @ElementCollection
    private Map<TotoResult, Double> quotes;
    @Enumerated
    private TotoResult totoResult;
    @Enumerated
    private TotoGameType totoGameType;
    private int matchDay;
    
    @Deprecated
    protected TotoMatch(){    	
    }

    public TotoMatch(String teamHome, String teamGuest, Map<TotoResult, Double> quotes, Date date, TotoGameType totoGameType, int matchDay) {
    	super(date);
	this.teamGuest = teamGuest;
	this.teamHome = teamHome;
	this.quotes = quotes;
	this.totoGameType = totoGameType;
	this.totoResult = TotoResult.NOT_PLAYED;
	this.matchDay = matchDay;
    }

    public String getTeamHome() {
	return teamHome;
    }

    public String getTeamGuest() {
	return teamGuest;
    }

    public TotoResult getResult() {
	return totoResult;
    }
    
    public TotoGameType getTotoGameType(){
    	return totoGameType;
    }
    
    public int getMatchDay(){
    	return matchDay;
    }

    public double getQuote(TotoResult result) {
	if (!quotes.containsKey(result))
	    return 1;

	return quotes.get(result);
    }
    
    @Override
    public GameType getType() {
	return GameType.TOTO;
    }

    @Override
    public String getTitle() {
	return String.format(teamHome + " : " + teamGuest, this.getDate());
    }

    public void setResult(TotoResult result) {
	if (result == TotoResult.NOT_PLAYED)
	    throw new IllegalArgumentException("You can't set the result of a game to NOT PLAYED!");

	if (getResult() != TotoResult.NOT_PLAYED)
	    throw new IllegalArgumentException("You can't set the result of a game, that already has been set!");

	this.totoResult = result;
	this.notifyObservers(true); // report that the game is finished in all
				    // things
    }



    public boolean isFinished() {
	return getResult() != TotoResult.NOT_PLAYED;
    }
}
