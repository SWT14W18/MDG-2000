package de.tudresden.swt14ws18.gamemanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TotoGame extends Game {
    private final static String title = "%1$d. Spieltag";
    private int round;

    private List<TotoMatch> matches;

    public TotoGame(Date date, int round, List<TotoMatch> matches) {
	super(date);
	this.round = round;
	this.matches = matches;
    }
    
    public TotoGame(Date date, int round, TotoMatch... matches)
    {
	this(date, round, Arrays.asList(matches));
    }

    @Override
    public GameType getType() {
	return GameType.TOTO;
    }

    @Override
    public String getTitle() {
	return String.format(title, round);
    }

    public List<TotoMatch> getMatches() {
	return matches;
    }

    @Override
    public boolean isFinished() {
	for (TotoMatch match : getMatches())
	    if (match.getResult() == TotoResult.NOT_PLAYED)
		return false;

	return true;
    }
}
