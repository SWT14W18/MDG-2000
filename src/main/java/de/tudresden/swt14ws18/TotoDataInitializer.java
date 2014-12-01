package de.tudresden.swt14ws18;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;

/**
 * Die Klasse TotoDataInitializer ist für das Füllen der Totodatenbank zuständig Die Funktion totoInitialize holt sich von der Seite
 * http://openligadb-json.herokuapp.com/api die Daten für alle Partien der 1. und 2. Bundesliga und des DFB-Pokals, erzeugt jeweils ein TotoMatch und
 * speichert es im TotoMatchRepository
 */

public class TotoDataInitializer {

    private TotoMatchRepository totoMatchRepository;
    private Map<TotoResult, Double> quotes;
    private String bl1 = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl1";
    private String bl2 = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl2";
    private String pokal = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=dfb2014nf";

    SimpleDateFormat inputSDF = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");
    DateTimeFormatter inputDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd;HH:mm:ss");

    public TotoDataInitializer(TotoMatchRepository totoMatchRepository) {
	this.totoMatchRepository = totoMatchRepository;
	Map<TotoResult, Double> quotes = new HashMap<>();
	quotes.put(TotoResult.DRAW, 2D);
	quotes.put(TotoResult.WIN_GUEST, 2D);
	quotes.put(TotoResult.WIN_HOME, 2D);
    }

    public void totoInitialize(TotoGameType totoGameType) throws IOException {
	String urlString = "";

	switch (totoGameType) {
	case BUNDESLIGA1:
	    urlString = bl1;
	    break;
	case BUNDESLIGA2:
	    urlString = bl2;
	    break;
	case POKAL:
	    urlString = pokal;
	    break;
	}

	URL url = new URL(urlString);
	HttpURLConnection request = (HttpURLConnection) url.openConnection();
	request.connect();

	JsonParser jp = new JsonParser();
	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject rootobj = root.getAsJsonObject();
	JsonArray matches = (JsonArray) rootobj.get("matchdata");
	String team1;
	String team2;
	String date;
	LocalDateTime gameDate;
	int matchDay;
	for (int i = 0; i < 306; i++) {

	    JsonObject match = (JsonObject) matches.get(i);
	    team1 = match.get("name_team1").getAsString();
	    team2 = match.get("name_team2").getAsString();
	    date = match.get("match_date_time").getAsString();
	    matchDay = match.get("group_order_id").getAsInt();
	    gameDate = LocalDateTime.parse(date.substring(0, 10) + ";" + date.substring(11, 19), inputDTF);
	    totoMatchRepository.save(new TotoMatch(team1, team2, quotes, gameDate, totoGameType, matchDay));

	}

    }
}
