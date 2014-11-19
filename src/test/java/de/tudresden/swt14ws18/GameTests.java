package de.tudresden.swt14ws18;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.Test;

import de.tudresden.swt14ws18.tipmanagement.LottoGame;
import de.tudresden.swt14ws18.tipmanagement.TotoGame;

public class GameTests {

    @Test
    public void testTotoGame() {
	TotoGame game = new TotoGame(new Date(), 11);
	TotoGame game2 = new TotoGame(new Date(), 12);

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
