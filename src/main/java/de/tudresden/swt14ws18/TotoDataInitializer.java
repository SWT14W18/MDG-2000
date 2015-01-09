package de.tudresden.swt14ws18;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private Random random;

    DateTimeFormatter inputDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd;HH:mm:ss");

    public TotoDataInitializer(TotoMatchRepository totoMatchRepository) {
    	this.totoMatchRepository = totoMatchRepository;
    	this.random = new Random();
    }
    
    
    public void totoInitialize(){
    	
    	if(checkConnection()){
    		try {
    			loadTotoMatches(TotoGameType.BUNDESLIGA1);
    			loadTotoMatches(TotoGameType.BUNDESLIGA2);
    			loadTotoMatches(TotoGameType.POKAL);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
	}
    

    private void loadTotoMatches(TotoGameType totoGameType) throws IOException {
	String urlString = "";
	int a = 0;

	switch (totoGameType) {
	case BUNDESLIGA1:
	    urlString = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl1";
	    a = 306;
	    break;
	case BUNDESLIGA2:
	    urlString = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl2";
	    a = 306;
	    break;
	case POKAL:
	    urlString = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=dfb2014nf";
	    a = 56;
	    break;
	}

	URL url = new URL(urlString);
	HttpURLConnection request = (HttpURLConnection) url.openConnection();
	request.connect();

	JsonParser jp = new JsonParser();
	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	JsonObject rootobj = root.getAsJsonObject();
	JsonArray matches = (JsonArray) rootobj.get("matchdata");
	TotoResult result = TotoResult.NOT_PLAYED;

	for (int i = 0; i < a; i++) {

	    JsonObject match = (JsonObject) matches.get(i);
	    
	    String team1 = match.get("name_team1").getAsString();
	    String team2 = match.get("name_team2").getAsString();
	    int matchDay = match.get("group_order_id").getAsInt();
	    String date = match.get("match_date_time").getAsString();
	    int jsonMatchId = match.get("match_id").getAsInt();
	    LocalDateTime gameDate = LocalDateTime.parse(date.substring(0, 10) + ";" + date.substring(11, 19), inputDTF);
	    
    	Map<TotoResult, Double> quotes = new HashMap<>();
    	
    	quotes.put(TotoResult.DRAW, (double) random.nextInt(10)+1);
    	quotes.put(TotoResult.WIN_GUEST, (double) random.nextInt(10)+1);
    	quotes.put(TotoResult.WIN_HOME, (double) random.nextInt(10)+1);
	   
	    TotoMatch totoMatch = new TotoMatch(team1, team2, quotes, gameDate, totoGameType, matchDay, jsonMatchId);
	    totoMatchRepository.save(totoMatch);
	    }
	    

    }
    
    private boolean checkConnection(){
		boolean connection = false;
    	try {
    		try {
    			URL url = new URL("http://openligadb-json.herokuapp.com/");
    			HttpURLConnection con = (HttpURLConnection) url.openConnection();
    			con.connect();
    			if (con.getResponseCode() == 200){
    				System.out.println("Connection established");
    				connection = true;
    			}
    		} catch (Exception exception) {
    			System.out.println("No Connection");
    			connection = false;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return connection;
    }
    
    public Map<TotoMatch, TotoResult> updateTotoResultMatchDay(int matchDay, TotoGameType totoGameType) throws IOException{
    	
    	Map<TotoMatch, TotoResult> resultMap = new HashMap<>();
    	
    	String leagueString="";
    	switch (totoGameType) {
    	case BUNDESLIGA1:
    	    leagueString = "bl1";
    	    break;
    	case BUNDESLIGA2:
    		leagueString = "bl2";
    	    break;
    	case POKAL:
    		leagueString = "dfb2014nf";
    	    break;
    	}
    	
    	URL url = new URL("http://openligadb-json.herokuapp.com/api/matchdata_by_group_league_saison?group_order_id="+matchDay+"&league_saison=2014&league_shortcut="+leagueString);
    	HttpURLConnection request = (HttpURLConnection) url.openConnection();
    	request.connect();
    	
    	JsonParser jp = new JsonParser();
    	JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
    	JsonObject rootobj = root.getAsJsonObject();
    	JsonArray matches = (JsonArray) rootobj.get("matchdata");
    	
    	for(TotoMatch totoMatch : totoMatchRepository.findByMatchDayAndTotoGameType(matchDay, totoGameType)){
    		
    		
    		for(int i = 0; i < matches.size(); i++){
    			JsonObject match = (JsonObject) matches.get(i);
    			if(totoMatch.getJsonMatchId()==match.get("match_id").getAsInt()){
    				if(match.get("match_is_finished").getAsBoolean()){
    					int scoreHome = match.get("points_team1").getAsInt();
    					int scoreGuest = match.get("points_team2").getAsInt();
    					totoMatch.setScoreHome(scoreHome);
    					totoMatch.setScoreGuest(scoreGuest);
    					TotoResult result = TotoResult.NOT_PLAYED;
    					switch(scoreHome > scoreGuest ? +1 : scoreHome < scoreGuest ? -1 : 0){
    						case -1:
    							result = TotoResult.WIN_GUEST;break;
    						case 0:
    							result = TotoResult.DRAW;break;
    						case 1:
    							result = TotoResult.WIN_HOME;break;
    					}
            		    resultMap.put(totoMatch, result);
    				}
    			}
    	    }
    	}
    	return resultMap;
    }

    
}
