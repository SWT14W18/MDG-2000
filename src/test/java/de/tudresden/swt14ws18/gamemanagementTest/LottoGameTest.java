package de.tudresden.swt14ws18.gamemanagementTest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.LottoResult;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;

/**
 * Dieser Test baut auf die funktionierenden RepositoryTests auf!
 * 
 * @author Reinhard_2
 *
 */

public class LottoGameTest extends AbstractIntegrationTest {

    @Autowired
    LottoMatchRepository matchRepo;

    @Test
    public void testConstructorLottoGame() {
        LocalDateTime time = Lotterie.getInstance().getTime().getTime();
        LottoGame g = new LottoGame(time);
        matchRepo.save(g);
        assertEquals(g, matchRepo.findByDate(time));
    }

    @Test
    public void testWinningPot() {
        LocalDateTime time = Lotterie.getInstance().getTime().getTime();
        Map<LottoResult, Double> winLevels = new HashMap<>();
        LottoGame g = new LottoGame(time);
        double winpot = 1;

        winLevels.put(LottoResult.NONE, 0 * winpot);
        winLevels.put(LottoResult.TWO_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.THREE, 0.1 * winpot);
        winLevels.put(LottoResult.THREE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.SIX, 0.1 * winpot);
        winLevels.put(LottoResult.SIX_SUPER, 0.2 * winpot);

        g.setWinningPot(winpot);
        matchRepo.save(g);
        assertEquals(g.getWinningPot(), winLevels);
    }

    @Test
    public void testResult() {
        LocalDateTime time = Lotterie.getInstance().getTime().getTime();
        LottoGame g = new LottoGame(time);
        matchRepo.save(g);
        assertEquals(g.getResult(), null);
        double winpot = 1;
        Map<LottoResult, Double> winLevels = new HashMap<>();

        winLevels.put(LottoResult.NONE, 0 * winpot);
        winLevels.put(LottoResult.TWO_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.THREE, 0.1 * winpot);
        winLevels.put(LottoResult.THREE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.SIX, 0.1 * winpot);
        winLevels.put(LottoResult.SIX_SUPER, 0.2 * winpot);

        g.setWinningPot(winpot);

        LottoNumbers num = new LottoNumbers(1, 2, 3, 4, 5, 6, 7);
        g.setResult(num);
        matchRepo.save(g);
        assertEquals(g.getResult(), num);
        assertEquals(g.isFinished(), true);
    }

}