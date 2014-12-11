package de.tudresden.swt14ws18.tips;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.LottoResult;

@Entity
public class LottoTip extends Tip {

    private static final double input = 1.00D;

    @ManyToOne
    private LottoGame lottoGame;
    private LottoNumbers numbers;
    @Enumerated
    private LottoResult result;

    @Deprecated
    public LottoTip() {

    }

    public LottoTip(LottoGame game, LottoNumbers numbers) {
        // game.addObserver(this);
        this.numbers = numbers;
        this.lottoGame = game;
    }

    public LottoGame getGame() {
        return lottoGame;
    }

    public LottoNumbers getNumbers() {
        return numbers;
    }

    public LottoResult getResult() {
        return result;
    }

    public void update(boolean arg) {
        if (arg)
            handleResult();
        else
            calculateResult();

        Lotterie.getInstance().getLottoTipRepository().save(this);
    }

    private void handleResult() {
        if (result == null && isValid())
            throw new IllegalStateException("Tried to resolve a tip, which has not be processed yet!");

        if (result == LottoResult.NONE || !isValid())
            return;

        Lotterie.getInstance().getLottoTipCollectionRepository().findByTips(this).update(this, false);
    }

    @Override
    public double getWinAmount() {
        return getGame().getWinAmount(getResult());
    }

    private void calculateResult() {
        if (getGame().getResult() == null)
            return;

        Lotterie.getInstance().getLottoTipCollectionRepository().findByTips(this).update(this, true);

        if (!isValid())
            return;

        result = getGame().getResult().compare(getNumbers());
        getGame().registerResult(result);
    }

    @Override
    public double getInput() {
        return input;
    }

    public String getNumbersAsString() {
        StringBuilder numbers_temp = new StringBuilder();

        numbers_temp.append("  Getippte Zahlen: ");

        for (int i : numbers.getNumbers()) {
            numbers_temp.append(i);
            numbers_temp.append(" ");
        }

        return numbers_temp.toString();
    }

    public String getSuperNumberAsString() {
        StringBuilder temp = new StringBuilder();

        temp.append("Getippte Superzahl: ");
        temp.append(numbers.getSuperNumber());

        return temp.toString();
    }

    public String getLottoGameTitleAsString() {
        return lottoGame.getTitle();
    }

    @Override
    public String toCustomString() {

        return lottoGame.getTitle() + getNumbersAsString();
    }

    public void setResult(LottoNumbers numbers2) {
        this.numbers = numbers2;
    }
}
