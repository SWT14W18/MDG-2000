package de.tudresden.swt14ws18.gamemanagement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import de.tudresden.swt14ws18.Lotterie;

@Entity
public class LottoGame extends Game {


    private static final String title = "Losung vom %1$s";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private LottoNumbers result = null;

    @ElementCollection
    private Map<LottoResult, Integer> resultMap = new HashMap<>();
    @ElementCollection
    private Map<LottoResult, Double> winLevels = new HashMap<>();

    public LottoGame(Date date) {
	super(date);
    }

    public void setWinningPot(double winningPot) {
	winLevels.put(LottoResult.NONE, 0 * winningPot);
	winLevels.put(LottoResult.TWO_SUPER, 0.1 * winningPot);
	winLevels.put(LottoResult.THREE, 0.1 * winningPot);
	winLevels.put(LottoResult.THREE_SUPER, 0.1 * winningPot);
	winLevels.put(LottoResult.FOUR, 0.1 * winningPot);
	winLevels.put(LottoResult.FOUR_SUPER, 0.1 * winningPot);
	winLevels.put(LottoResult.FIVE, 0.1 * winningPot);
	winLevels.put(LottoResult.FIVE_SUPER, 0.1 * winningPot);
	winLevels.put(LottoResult.SIX, 0.1 * winningPot);
	winLevels.put(LottoResult.SIX_SUPER, 0.2 * winningPot);
    }

    public LottoNumbers getResult() {
	return result;
    }

    public void setResult(LottoNumbers result) {
	if (result == null)
	    throw new IllegalArgumentException("You can't set the result of a game to NOT PLAYED!");

	if (getResult() != null)
	    throw new IllegalArgumentException("You can't set the result of a game, that already has been set!");

	this.result = result;
	this.notifyObservers(false); // report that game is ready and that tips
				     // please report their result
	this.notifyObservers(true); // report that the game now knows who won
				    // how much

	Lotterie.getInstance().setNextLottoPot(this); //TODO noch nicht optimal
    }

    @Override
    public String getTitle() {
	return String.format(title, dateFormat.format(getDate()));
    }

    @Override
    public GameType getType() {
	return GameType.LOTTO;
    }

    public void registerResult(LottoResult result2) {
	if (!resultMap.containsKey(result2))
	    resultMap.put(result2, 0);

	resultMap.put(result2, resultMap.get(result2) + 1);
    }

    public double getWinAmount(LottoResult r) {
	double win = winLevels.get(r);
	int number = resultMap.get(r);

	return new BigDecimal(win / number).setScale(2, RoundingMode.FLOOR).doubleValue();
    }

    public double getRemainingPot() {
	double result = 0;

	for (LottoResult r : LottoResult.values())
	    if (!resultMap.containsKey(r) || resultMap.get(r) == 0)
		result += winLevels.get(r);

	return result;
    }

    @Override
    public boolean isFinished() {
	return getResult() != null;
    }

}
