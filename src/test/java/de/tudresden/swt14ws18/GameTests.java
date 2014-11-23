package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.TotoGame;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;

public class GameTests {

    @Test
    public void testTotoGame() {
	Map<TotoResult, Double> quotes = new HashMap<>();
	quotes.put(TotoResult.DRAW, 2D);
	quotes.put(TotoResult.WIN_GUEST, 2D);
	quotes.put(TotoResult.WIN_HOME, 2D);
	
	TotoGame game = new TotoGame(new Date(), 11, new TotoMatch("FC Blue", "Rot SC", quotes, new Date(), TotoGameType.POKAL, 1));
	TotoGame game2 = new TotoGame(new Date(), 12, new TotoMatch("Gelber FC ", "FC Grün", quotes, new Date(), TotoGameType.POKAL, 1));

	assertEquals(game.getTitle(), "11. Spieltag");
	assertNotEquals(game2.getTitle(), "11. Spieltag");
    }

    @Test
    public void testLottoGame() {
	//1416393277061L = 19.11.2014, the day the test was created
	LottoGame game = new LottoGame(new Date(1416393277061L));
	LottoGame game2 = new LottoGame(new Date(1416393277061L));

	assertEquals(game.getTitle(), "Losung vom 19.11.2014");
	assertNotEquals(game2.getTitle(), "Losung vom 18.11.2014");
    }
}
