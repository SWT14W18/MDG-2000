package de.tudresden.swt14ws18.tips;

import java.util.Observable;
import java.util.Observer;

public abstract class Tip extends Observable implements Observer {

    private boolean valid = true;

    public Tip() {
    }

    public boolean isValid() {
	return valid;
    }
    
    public void invalidate(boolean valid) {
	this.valid = valid;
    }
    
    public abstract Finishable getGame();
    
    public abstract double getInput();
    
    public abstract double getWinAmount();
    
    public boolean isFinished() {
	return getGame().isFinished();
    }
    
}
