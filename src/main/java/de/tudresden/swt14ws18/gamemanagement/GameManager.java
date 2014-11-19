package de.tudresden.swt14ws18.gamemanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private Map<Long, Game> games = new HashMap<>();

    public Game getGame(long id) {
	return games.get(id);
    }

    public void addGame(Game game) {
	games.put(game.getId(), game);
    }

    public Collection<Game> getGames(GameType type) {
	List<Game> list = new ArrayList<>();

	for (Game value : games.values())
	    if (value.getType() == type)
		list.add(value);

	return list;
    }
    
    public void test(LottoGame game)
    {
	LottoGame result = null;
	
	for(Game g : games.values())
	{
	    if(g.getType() != GameType.LOTTO)
		continue;
	    
	    if(!game.getDate().after(g.getDate()))
		continue;
	    
	    if(result == null || g.getDate().before(result.getDate()))
		result = (LottoGame) g;
	}

	
    }
}
