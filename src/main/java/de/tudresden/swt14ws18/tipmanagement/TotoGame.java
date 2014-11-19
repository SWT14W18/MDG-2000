package de.tudresden.swt14ws18.tipmanagement;

import java.util.Date;

public class TotoGame extends Game {
    private final static String title = "%1$d. Spieltag";
    private int round;
    
    public TotoGame(Date date, int round)
    {
	super(date);
	this.round = round;
    }
    
    
    @Override
    public GameType getType() {
	return GameType.TOTO;
    }

    @Override
    public String getTitle() {
	return String.format(title, round);
    }

}
