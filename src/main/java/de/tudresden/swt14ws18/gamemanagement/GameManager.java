package de.tudresden.swt14ws18.gamemanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GameManager {
    private static final double LOTTO_PRICE = 1.00D;
    private static final double INPUT_INTO_POT = 0.9D;
    private Map<Long, Game> games = new HashMap<>();

    public Game getGame(long id) {
	return games.get(id);
    }

    public void addGame(Game game) {
	System.out.println(game.getId() + " " + game.getType() + " " + game.isFinished());
	games.put(game.getId(), game);
    }

    public Collection<Game> getGames(GameType type) {
	List<Game> list = new ArrayList<>();

	for (Game value : games.values())
	    if (value.getType() == type)
		list.add(value);

	return list;
    }
    
    public List<Game> getUnfinishedGames(GameType type) {
	List<Game> list = new ArrayList<>();

	for (Game value : games.values())
	{
	    System.out.println(value.getType() + " " + value.isFinished());
	    if (value.getType() == type && !value.isFinished())
		list.add(value);
	    
	}

	return list;
    }

    public void setNextLottoPot(LottoGame game) {
	LottoGame result = null;

	for (Game g : games.values()) {
	    if (g.getType() != GameType.LOTTO)
		continue;

	    if (!game.getDate().after(g.getDate()))
		continue;

	    if (result == null || g.getDate().before(result.getDate()))
		result = (LottoGame) g;
	}

	if (result != null)
	    result.setWinningPot(game.getRemainingPot() + game.countObservers() * LOTTO_PRICE * INPUT_INTO_POT);
    }
}
