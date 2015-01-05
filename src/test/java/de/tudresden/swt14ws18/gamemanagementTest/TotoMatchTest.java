package de.tudresden.swt14ws18.gamemanagementTest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;

/**
 * Dieser Test befasst sich mit dem Testen der TotoMatch-Funktionalität.
 * Da wir die TotoMatches in Echtzeit aus dem Web laden, wird hier lediglich überprüft, ob ein geladenes
 * Spiel im Repository ordnungsgemäß gespeichert wird.
 * 
 * @author Reinhard_2
 *
 */

public class TotoMatchTest extends AbstractIntegrationTest {
	
	@Autowired TotoMatchRepository matchRepo;
	
	@Rule public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testConstructorTotoMatch() {
		LocalDateTime ldt = Lotterie.getInstance().getTime().getTime();
		Map<TotoResult, Double> quotes = new HashMap<>();
		quotes.put(TotoResult.WIN_HOME, 1.0D);
		quotes.put(TotoResult.WIN_GUEST, 1.0D);
		quotes.put(TotoResult.DRAW, 1.0D);
		TotoMatch totoMatch = new TotoMatch("teamHome", "teamGuest", quotes, ldt, TotoGameType.BUNDESLIGA1, 1);
		matchRepo.save(totoMatch);		
		assertEquals(totoMatch, matchRepo.findOne(totoMatch.getId()));
	}

	@Test
	public void testSetResult() {
		LocalDateTime ldt = Lotterie.getInstance().getTime().getTime();
		Map<TotoResult, Double> quotes = new HashMap<>();
		quotes.put(TotoResult.WIN_HOME, 1.0D);
		quotes.put(TotoResult.WIN_GUEST, 1.0D);
		quotes.put(TotoResult.DRAW, 1.0D);
		TotoMatch totoMatch = new TotoMatch("teamHome", "teamGuest", quotes, ldt, TotoGameType.BUNDESLIGA1, 1);
		matchRepo.save(totoMatch);
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("You can't set the result of a game to NOT PLAYED!");
		totoMatch.setResult(TotoResult.NOT_PLAYED);		

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("You can't set the result of a game, that already has been set!");		
		totoMatch.setResult(TotoResult.DRAW);
		totoMatch.setResult(TotoResult.DRAW);

	}

}
