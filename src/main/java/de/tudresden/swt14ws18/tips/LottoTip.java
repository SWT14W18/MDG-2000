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

	// TODO Auto-generated method stub

    }

    private void handleResult() {
	if (result == null)
	    throw new IllegalStateException("Tried to resolve a tip, which has not be processed yet!");

	if (result == LottoResult.NONE)
	    return;
	
	double amount = getGame().getWinAmount(result);
	
	// TODO give win

    }

    private void calculateResult() {
	if (getGame().getResult() == null)
	    return;

	// TODO remove input

	result = getGame().getResult().compare(getNumbers());
	getGame().registerResult(result);
    }
}
