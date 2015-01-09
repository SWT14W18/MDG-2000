package de.tudresden.swt14ws18.gamemanagement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.tips.TotoTip;

/**
 * Repräsentiert ein Fussballspiel.
 */
@Entity
public class TotoMatch extends Game {

    private String teamHome;
    private String teamGuest;
    private int scoreHome;
    private int scoreGuest;
    private int jsonMatchId;
    @ElementCollection
    private Map<TotoResult, Double> quotes;
    @ElementCollection
    @Deprecated
    private Map<TotoResult, Double> resultInput;
    @Enumerated
    private TotoResult totoResult;
    @Enumerated
    private TotoGameType totoGameType;
    private int matchDay;
    @Deprecated
    private Double totalInput = 0.0D; // TODO entfernen, sollte über repository abgefragt werden!

    @Deprecated
    protected TotoMatch() {
    }

    public TotoMatch(String teamHome, String teamGuest, Map<TotoResult, Double> quotes, LocalDateTime date, TotoGameType totoGameType, int matchDay, int jsonMatchId) {
        super(date);
        this.teamGuest = teamGuest;
        this.teamHome = teamHome;
        this.quotes = quotes;
        this.totoGameType = totoGameType;
        this.totoResult = TotoResult.NOT_PLAYED;
        this.matchDay = matchDay;
        this.jsonMatchId = jsonMatchId;
        resultInput = new HashMap<>();
        resultInput.put(TotoResult.WIN_HOME, 0.0D);
        resultInput.put(TotoResult.WIN_GUEST, 0.0D);
        resultInput.put(TotoResult.DRAW, 0.0D);
        this.scoreHome = -1;
        this.scoreGuest = -1;
    }

    /**
     * Hole den Namen des Heim Teams.
     * 
     * @return der Name des Heim Teams
     */
    public String getTeamHome() {
        return teamHome;
    }

    /**
     * Hole den Namen des Gast Teams.
     * 
     * @return der Name des Gast Teams
     */
    public String getTeamGuest() {
        return teamGuest;
    }
    
    public int getJsonMatchId(){
    	return jsonMatchId;
    }

    /**
     * Hole das Ergebnis dieser Partie, NOT_PLAYED wenn noch nicht gespielt.
     * 
     * @return das Ergebnis der Partie
     */
    public TotoResult getResult() {
        return totoResult;
    }

    /**
     * Hole den Typ der TotoGames (Liga).
     * 
     * @return die Liga des Spiels
     */
    public TotoGameType getTotoGameType() {
        return totoGameType;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public int getScoreGuest() {
        return scoreGuest;
    }

    public void setScoreGuest(int scoreGuest) {
        this.scoreGuest = scoreGuest;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public Map<TotoResult, Double> getResultInput() {
        return resultInput;
    }

    public Double getTotalInput() {
        return totalInput;
    }

    /**
     * Hole den Spieltag in der jeweiligen Liga
     * 
     * @return der Spieltag als int
     */
    public int getMatchDay() {
        return matchDay;
    }

    /**
     * Hole die Quote für ein gewisses Ergebnis. Falls das Ergebnis nicht definiert ist, geb diese Method 1 zurück.
     * 
     * @param result
     *            das mögliche Ergebnis
     * @return die Quote, 1 falls nicht definiert.
     */
    public double getQuote(TotoResult result) {
        if (!quotes.containsKey(result))
            return 0.4;

        return quotes.get(result);
    }

    /**
     * Fügt den Input zur Statistik hinzu.
     * 
     * Deprecated, da dies eigentlich nicht im Match passieren sollte.
     * 
     * @param totoResult
     *            das Ergebnis, zu welchen der Input gerechnet werden soll
     * @param input
     *            der Betrag
     */
    @Deprecated
    public void addInput(TotoResult totoResult, Double input) {
        resultInput.put(totoResult, resultInput.get(totoResult) + input);
        totalInput += input;
    }

    /**
     * Setze die Quoten des Spieles.
     * 
     * @param quotes
     *            eine Map mit Werten des TotoResult enums als Key und einem Double als value.
     */
    public void setQuotes(Map<TotoResult, Double> quotes) {
        this.quotes = quotes;
    }

    @Override
    public GameType getType() {
        return GameType.TOTO;
    }

    @Override
    public String getTitle() {
        return String.format(teamHome + " : " + teamGuest, this.getDate());
    }

    /**
     * Setze das Ergebnis des Matches. Wenn das Ergebnis gesetzt wird, setzt automatisch die Gewinnberechnung und Ausschüttung in Kraft.
     * 
     * @param result
     *            das Ergebnis der Partie
     * @throws IllegalArgumentException
     *             falls result == NOT_PLAYED oder result schon gesetzt wurde.
     */
    public void setResult(TotoResult result) {
        if (result == TotoResult.NOT_PLAYED)
            throw new IllegalArgumentException("You can't set the result of a game to NOT PLAYED!");

        if (getResult() != TotoResult.NOT_PLAYED)
            throw new IllegalArgumentException("You can't set the result of a game, that already has been set!");

        this.totoResult = result;

        for (TotoTip tip : Lotterie.getInstance().getTotoTipRepository().findByTotoMatch(this)) {
            tip.update(); // report that the game is finished in all things
        }
    }

    @Override
    public boolean isFinished() {
        return getResult() != TotoResult.NOT_PLAYED;
    }
}
