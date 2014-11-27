package de.tudresden.swt14ws18.tips;

import java.util.Observable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;

@Entity
public class TotoTip extends Tip {

    @Enumerated
    private TotoResult result;    
    @ManyToOne
    private TotoMatch game;
    private double input;

    public TotoTip(TotoMatch game, TotoResult result, double input) {
	game.addObserver(this);
	this.game = game;
	this.result = result;
	this.input = input;
    }

    public TotoMatch getGame() {
	return game;
    }

    public TotoResult getResult() {
	return result;
    }

    public double getInput() {
	return input;
    }

    public double getWinAmount() {
	return game.getResult() == getResult() ? input * game.getQuote(getResult()) : 0;
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
}
