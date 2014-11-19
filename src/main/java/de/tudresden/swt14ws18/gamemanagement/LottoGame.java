package de.tudresden.swt14ws18.gamemanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LottoGame extends Game {

    private static final String title = "Losung vom %1$s";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
	    "dd.MM.yyyy");

    private LottoNumbers result = null;
    
    public LottoGame(Date date) {
	super(date);
    }
    
    public LottoNumbers getResult() {
	return result;
    }

    public void setResult(LottoNumbers result)
    {
	if (result == null)
	    throw new IllegalArgumentException(
		    "You can't set the result of a game to NOT PLAYED!");

	if (getResult() != null)
	    throw new IllegalArgumentException(
		    "You can't set the result of a game, that already has been set!");

	this.result = result;
	this.notifyObservers();
	
	
    }
    @Override
    public String getTitle() {
	return String.format(title, dateFormat.format(getDate()));
    }

    @Override
    public GameType getType() {
	return GameType.LOTTO;
    }

}
