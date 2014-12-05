package de.tudresden.swt14ws18.tips;

import java.util.Observable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;

@Entity
public class TotoTip extends Tip {

    @Enumerated
    private TotoResult result;
    @OneToOne
    private TotoMatch totoMatch;
    private double input;

    @Deprecated
    public TotoTip() {

    }

    public TotoTip(TotoMatch totoMatch, TotoResult result, double input) {
	totoMatch.addObserver(this);
	this.totoMatch = totoMatch;
	this.result = result;
	this.input = input;
	totoMatch.addInput(result, input);
    }

    public TotoMatch getGame() {
	return totoMatch;
    }

    public TotoResult getResult() {
	return result;
    }

    public double getInput() {
	return input;
    }

    public double getWinAmount() {
	return totoMatch.getResult() == getResult() ? input * totoMatch.getQuote(getResult()) : 0;
    }

    @Override
    public void update(Observable o, Object arg) {
	if (getGame().getResult() == TotoResult.NOT_PLAYED)
	    return;

	notifyObservers(true);

	if (!isValid())
	    return;

	notifyObservers(false);
    }
    
    public String getMatchDateAsString(){
        return totoMatch.getDateString();
    }

    public String getMatchTitleAsString(){
        return totoMatch.getTitle();
    }
    
    public String getInputAsString(){
        return String.valueOf(input);
    }
    
    public String getQuoteAsString(){
        return String.valueOf(totoMatch.getQuote(result));
    }
    
    public String getResultAsString(){
        StringBuilder temp = new StringBuilder();
        
        temp.append(result);
        
        return temp.toString();
    }
    
    @Override
    public String toCustomString() {
        StringBuilder temp = new StringBuilder();
        
        temp.append("Datum: ");
        temp.append(totoMatch.getDateString());
        temp.append("<br/>Spiel:  ");
        temp.append(totoMatch.getTitle());
        temp.append("<br/>  Einsatz:  ");
        temp.append(input);
        temp.append("€");
        temp.append("<br/>  Quote:   ");
        temp.append(totoMatch.getQuote(result));
        
        
        
        
        return temp.toString();
    }
}
