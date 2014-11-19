package de.tudresden.swt14ws18.tipmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;



public class LottoGame extends Game {

    private static final String title = "Losung vom %1$s";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    public LottoGame(Date date) {
	super(date);
    }

    @Override
    public String getTitle() {
	return String.format(title, dateFormat.format(getDate()));
    }

    @Override
    public GameType getType() {
	return GameType.TOTO;
    }

}
