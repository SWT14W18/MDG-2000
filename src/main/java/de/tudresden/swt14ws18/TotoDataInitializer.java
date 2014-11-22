package de.tudresden.swt14ws18;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoMatchRepository;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;


public class TotoDataInitializer {
	
	private TotoMatchRepository totoMatchRepository;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	public TotoDataInitializer(TotoMatchRepository totoMatchRepository){
		this.totoMatchRepository = totoMatchRepository;
	}
		

	public void totoInitialize() throws IOException, ParseException{
		
		Map<TotoResult, Double> quotes = new HashMap<>();
		quotes.put(TotoResult.DRAW, 2D);
		quotes.put(TotoResult.WIN_GUEST, 2D);
		quotes.put(TotoResult.WIN_HOME, 2D);
		
	    URL url = new URL("http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl1");
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.connect();

	    JsonParser jp = new JsonParser(); 
	    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	    JsonObject rootobj = root.getAsJsonObject(); 
	    JsonArray matches = (JsonArray) rootobj.get("matchdata");
	    String team1;
	    String team2;
	    String date;
	    Date gameDate;
	    for (int i=0;i<306;i++){

	    			JsonObject match = (JsonObject) matches.get(i);
	    			team1 = match.get("name_team1").getAsString();	
	    			team2 = match.get("name_team2").getAsString();
	    			date = match.get("match_date_time").getAsString();
	    			gameDate = sdf.parse(date.substring(8, 10)+"."+date.substring(5, 7)+"."+date.substring(0, 4));
	    			totoMatchRepository.save(new TotoMatch(team1, team2, quotes, gameDate, TotoGameType.BUNDESLIGA1));

	    }
	    
	}
}
