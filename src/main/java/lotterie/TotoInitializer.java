package lotterie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class TotoInitializer {
		

	public static void main(String[] args) throws IOException{
		
		String sURL = "http://openligadb-json.herokuapp.com/api/matchdata_by_league_saison?league_saison=2014&league_shortcut=bl1";

	    URL url = new URL(sURL);
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.connect();

	    JsonParser jp = new JsonParser(); 
	    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	    JsonObject rootobj = root.getAsJsonObject(); 
	    JsonArray Matches = (JsonArray) rootobj.get("matchdata");
	    String team1;
	    String team2;
	    String date;
	    for (int i=0;i<306;i++){
	    	try {
	    			JsonObject Match = (JsonObject) Matches.get(i);
	    			team1 = Match.get("name_team1").getAsString();	
	    			team2 = Match.get("name_team2").getAsString();
	    			date = Match.get("match_date_time").getAsString();
	    			SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
	    			Date actualDate = sdf.parse("19.11.2014");
	    			Date playDate = sdf.parse(date.substring(8, 10)+"."+date.substring(5, 7)+"."+date.substring(0, 4));
	    			String stringDate = sdf.format(playDate );
//	    			if (playDate.before(actualDate)) {
//	    				JsonObject results = (JsonObject) Match.get("match_results");
//	    				JsonArray result = (JsonArray) results.get("match_result");
//	    				JsonObject finalResult = (JsonObject) result.get(0);
//	    				String pointsTeam1 = finalResult.get("points_team1").getAsString();	
//	    				String pointsTeam2 = finalResult.get("points_team2").getAsString();	
//	    				
//	    				System.out.println(stringDate + " - " + team1 + " : " + team2 + " - " + pointsTeam1 + " : " + pointsTeam2);
//	    			}
//	    			else {
	    				System.out.println(stringDate + " - " + team1 + " : " + team2);
//	    			}
	    	}
	    	catch(ParseException ex){
	    		ex.printStackTrace();
	    	}
	    }
	}
}
