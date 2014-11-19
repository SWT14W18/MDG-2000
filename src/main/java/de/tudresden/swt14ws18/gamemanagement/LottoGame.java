package de.tudresden.swt14ws18.gamemanagement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public class LottoGame extends Game {

    private GameManager manager;

    private static final String title = "Losung vom %1$s";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private LottoNumbers result = null;

    private Map<LottoResult, Integer> resultMap = new HashMap<>();
    private Map<LottoResult, Double> winLevels = new HashMap<>();

    public LottoGame(Date date) {
	super(date);
    }

    @Autowired
    public void setGameManager(GameManager manager) {
	this.manager = manager;
    }

    public void setWinningPot(double winningPot) {
	// TODO missing formula
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

	manager.setNextLottoPot(this);
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

}
