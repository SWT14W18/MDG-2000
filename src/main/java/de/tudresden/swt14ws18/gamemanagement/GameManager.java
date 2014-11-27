package de.tudresden.swt14ws18.gamemanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GameManager {
    private static final double LOTTO_PRICE = 1.00D;
    private static final double INPUT_INTO_POT = 0.9D;
    private Map<Long, Game> games = new HashMap<>();
    private LottoMatchRepository lottoMatchRepository;
    private TotoMatchRepository totoMatchRepository;
    
    public void setTotoMatchRepository(TotoMatchRepository totoMatchRepository){
    	this.totoMatchRepository = totoMatchRepository;
    }
    
    public List<TotoMatch> getTotoMatchByDate(Date date){
    	return this.totoMatchRepository.findByDate(date);
    }
    
    public List<TotoMatch> getTotoMatchByTeam(String team){
    	return this.totoMatchRepository.findByTeamHomeOrTeamGuest(team, team);
    }
    
    public List<TotoMatch> getTotoMatchByMatchDay(int matchDay){
    	return this.totoMatchRepository.findByMatchDay(matchDay);
    }

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
    
    public List<TotoMatch> getUnfinishedTotoMatches(){
    	return this.totoMatchRepository.findByTotoResult(TotoResult.NOT_PLAYED) ;	
    }
    
    public List<Game> getUnfinishedGames(GameType type) {
	List<Game> list = new ArrayList<>();

	for (Game value : games.values())
	    if (value.getType() == type && !value.isFinished())
		list.add(value);

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
