package de.tudresden.swt14ws18.tips;

import java.util.Observable;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.LottoResult;

public class LottoTip extends Tip {

    private static final double input = 1.00D;

    private LottoGame game;
    private LottoNumbers numbers;
    private LottoResult result;

    public LottoTip(LottoGame game, LottoNumbers numbers) {
	game.addObserver(this);
	this.numbers = numbers;
	this.game = game;
    }

    public LottoGame getGame() {
	return game;
    }

    public LottoNumbers getNumbers() {
	return numbers;
    }

    public LottoResult getResult() {
	return result;
    }

    @Override
    public void update(Observable o, Object arg) {
	if ((Boolean) arg)
	    handleResult();
	else
	    calculateResult();
    }

    private void handleResult() {
	if (result == null)
	    throw new IllegalStateException("Tried to resolve a tip, which has not be processed yet!");

	if (result == LottoResult.NONE || isValid())
	    return;

	notifyObservers(false);
    }

    @Override
    public double getWinAmount() {
	return getGame().getWinAmount(getResult());
    }

    private void calculateResult() {
	if (getGame().getResult() == null)
	    return;

	notifyObservers(true);

	if (!isValid())
	{
	    getGame().deleteObserver(this);
	    return;
	}

	result = getGame().getResult().compare(getNumbers());
	getGame().registerResult(result);
    }
    
    @Override
    public double getInput() {
	return input;
    }
}
