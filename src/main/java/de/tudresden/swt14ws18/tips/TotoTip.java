package de.tudresden.swt14ws18.tips;

import java.util.Observable;

import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;

public class TotoTip extends Tip {

    private TotoResult result;
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
    
    public double getInput()
    {
	return input;
    }

    @Override
    public void update(Observable o, Object arg) {
	if(getGame().getResult() == TotoResult.NOT_PLAYED)
	    return;
	
	//TODO game played - take the money
	
	if(getGame().getResult() != result)
	    return;
	
	//TODO player won - give him his money
	
    }
}
