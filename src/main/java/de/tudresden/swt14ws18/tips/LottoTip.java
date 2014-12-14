package de.tudresden.swt14ws18.tips;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.LottoResult;

/**
 * Repräsentiert einen Lottotip
 */
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
        this.numbers = numbers;
        this.lottoGame = game;
    }

    @Override
    public LottoGame getGame() {
        return lottoGame;
    }

    /**
     * Hole die gesetzten Lottozahlen
     * 
     * @return LottoNumbers dieses Tips
     */
    public LottoNumbers getNumbers() {
        return numbers;
    }

    /**
     * Hole die Gewinnklasse des Tipps
     * 
     * @return das LottoResult des Tipps, null falls der Tipp noch nicht ausgewertet wurde.
     */
    public LottoResult getResult() {
        return result;
    }

    /**
     * Überreste des Oberserver Patterns.
     * 
     * Diese Methode wird vom Spiel aufgerufen um zu zu erkennen zu geben, dass dieses abgeschlossen wurde. Es gibt dabei 2 verschiedene Modi. Der
     * "false" mode, ist ein Trockenlauf, der das Ergebnis des Spieles mit dem Tip vergleicht und das Spiel soweit updated, dass es weiß, wieviel Geld
     * pro Gewinnklasse ausgezahlt werden muss. Außerdem wird hier vom Tippschein überprüft, ob der Tip überhaupt gewertet wird.
     * 
     * Der "true" mode meldet an den Tippschein, dass der Tipp fertig ist und weiterverarbeitet werden kann.
     * 
     * Nach jedem Aufruf dieser Methode, wird der Tip im Repository gespeichert.
     * 
     * @param mode
     *            der Modus, des Updates
     */
    public void update(boolean mode) {
        if (mode)
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
