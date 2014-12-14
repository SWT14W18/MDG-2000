package de.tudresden.swt14ws18.gamemanagement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Repräsentiert eine Lotto Ziehung
 */
@Entity
public class LottoGame extends Game {

    private static final String title = "Losung vom %1$s";

    private LottoNumbers result = null;
    private Double totalWinningPot = null;

    @ElementCollection
    private Map<LottoResult, Integer> resultMap = new HashMap<>();
    @ElementCollection
    private Map<LottoResult, Double> winLevels = new HashMap<>();

    @Deprecated
    public LottoGame() {

    }

    public LottoGame(LocalDateTime date) {
        super(date);
    }

    /**
     * Setze den Gewinnpot basierend auf einen Inputwert. Die Gewinnklasse NONE erhält 0% des Pots, die Gewinnklasse SIX_SUPER (Jackpot) erhält 20%
     * des Pots. Der rest wird gleichmäßig zwischen den übrigen Gewinnklassen aufgeteilt. (10% pro Klasse)
     * 
     * @param winningPot
     *            der Geldbetrag der in dieser Lottoziehung ausgeschütet wird.
     */
    public void setWinningPot(double winningPot) {
    	this.totalWinningPot = winningPot;
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

    public Map<LottoResult, Double> getWinningPot() {
        return winLevels;
    }

    /**
     * Hole die gezogenen Zahlen, null fall noch nicht gezogen.
     * 
     * @return die Zahlen als LottoNumbers
     */
    public LottoNumbers getResult() {
        return result;
    }
    
    public Double getTotalWinningPot(){
    	return totalWinningPot;
    }

    /**
     * Setze die gezogenen Zahlen des Spiel. Funktioniert nur, falls das Spiel noch kein Ergebnis gesetzt hat.
     * 
     * Wenn das Ergebnis gesetzt wird, findet automatisch die Gewinnausschüttung statt. Außerdem wird der Pot für das nächste Lotto Spiel gesetzt.
     * 
     * @param result
     *            die gezogenen Zahlen, die eingesetzt werden sollen.
     * @throws IllegalArgumentException
     *             falls result == null oder das Ergebnis schon gesetzt wurde.
     */
    public void setResult(LottoNumbers result) {
        if (result == null)
            throw new IllegalArgumentException("You can't set the result of a game to NOT PLAYED!");

        if (getResult() != null)
            throw new IllegalArgumentException("You can't set the result of a game, that already has been set!");

        this.result = result;

        for (LottoTip tip : Lotterie.getInstance().getLottoTipRepository().findByLottoGame(this)) {
            tip.update(false); // report that game is ready and that tips please report their result
            tip.update(true); // report that the game now knows who won how much
        }

        Lotterie.getInstance().setNextLottoPot(this);
    }

    @Override
    public String getTitle() {
        return String.format(title, Constants.OUTPUT_DTF.format(getDate()));
    }

    @Override
    public GameType getType() {
        return GameType.LOTTO;
    }

    /**
     * Registriere das Ergebnis eines Tipps für die Gewinnberechnung.
     * 
     * @param result2
     *            das zu registrierende Ergebnis
     */
    public void registerResult(LottoResult result2) {
        if (!resultMap.containsKey(result2))
            resultMap.put(result2, 0);

        resultMap.put(result2, resultMap.get(result2) + 1);
    }

    /**
     * Berechne den Gewinn der gegebenen Gewinnklasse. Das Ergbnis wird auf 2 Nachkommastellen abgerundet.
     * 
     * @param r
     *            die Gewinnklasse für die der Gewinn zu berechnen ist.
     * @return der Gewinn als double, abgerundet auf 2 Nachkommestellen.
     */
    public double getWinAmount(LottoResult r) {
        double win = winLevels.get(r);
        int number = resultMap.get(r);

        return new BigDecimal(win / number).setScale(2, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * Berechne wieviel Geld dieser Ziehung nicht ausgezahlt wurde, d.h. in welche Klasse es keinen Gewinner gab.
     * 
     * @return Der verbleibende Pot als double.
     */
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

    /**
     * Hole den Jackpot dieses Spieles. Falls der Pot noch nicht gesetzt wurde, wird "Unbekannt" zurückgegeben.
     * 
     * @return Den Jackpot des Spieles oder "Unbekannt", falls noch nicht bekannt.
     */
    public String getJackpot() {
        return winLevels != null && winLevels.containsKey(LottoResult.SIX_SUPER) ? Constants.MONEY_FORMAT.format(20000.0D) : "Unbekannt";
    }

}
