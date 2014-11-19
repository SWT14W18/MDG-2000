package de.tudresden.swt14ws18.tips;

import java.util.Observable;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;

public class LottoTip extends Tip {
    
    private LottoGame game;
    private LottoNumbers numbers;
    
    public LottoTip(LottoGame game, LottoNumbers numbers)
    {
	game.addObserver(this);
	this.numbers = numbers;
	this.game = game;
    }
    @Override
    public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub

    }

}
