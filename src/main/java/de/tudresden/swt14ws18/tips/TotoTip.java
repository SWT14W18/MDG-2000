package de.tudresden.swt14ws18.tips;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.util.Constants;

@Entity
public class TotoTip extends Tip {

    @Enumerated
    private TotoResult result;
    @OneToOne
    private TotoMatch totoMatch;
    private double input;

    @Deprecated
    public TotoTip() {

    }

    public TotoTip(TotoMatch totoMatch, TotoResult result, double input) {
        this.totoMatch = totoMatch;
        this.result = result;
        this.input = input;
        totoMatch.addInput(result, input);
    }

    public TotoMatch getGame() {
        return totoMatch;
    }

    public TotoResult getResult() {
        return result;
    }

    public double getInput() {
        return input;
    }

    public double getWinAmount() {
        return totoMatch.getResult() == getResult() ? input * totoMatch.getQuote(getResult()) : 0;
    }

    public void update() {
        if (getGame().getResult() == TotoResult.NOT_PLAYED)
            return;

        Lotterie.getInstance().getTotoTipCollectionRepository().findByTips(this).update(this, true);

        if (!isValid())
            return;

        Lotterie.getInstance().getTotoTipCollectionRepository().findByTips(this).update(this, false);
    }

    public String getMatchDateAsString() {
        return totoMatch.getDateString();
    }

    public String getMatchTitleAsString() {
        return totoMatch.getTitle();
    }

    public String getInputAsString() {
        return Constants.MONEY_FORMAT.format(input);
    }

    public String getQuoteAsString() {
        return String.valueOf(totoMatch.getQuote(result));
    }

    public String getResultAsString() {
        StringBuilder temp = new StringBuilder();

        temp.append(result);

        return temp.toString();
    }

    @Override
    public String toCustomString() {
        StringBuilder temp = new StringBuilder();

        temp.append("Datum: ");
        temp.append(totoMatch.getDateString());
        temp.append("<br/>Spiel:  ");
        temp.append(totoMatch.getTitle());
        temp.append("<br/>  Einsatz:  ");
        temp.append(Constants.MONEY_FORMAT.format(input));
        temp.append("€");
        temp.append("<br/>  Quote:   ");
        temp.append(totoMatch.getQuote(result));

        return temp.toString();
    }

    public void setResult(TotoResult result) {
        this.result = result;
    }
}
