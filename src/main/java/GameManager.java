import java.util.List;
import java.util.Map;


public class GameManager {
	private long idCounter = 0;
	private Map<Long, Game> games;
	private List<TotoGame> totoGame;
	private List<LottoGame> lottoGame;
	
	public List<TotoGame> getTotoGames()
	{
		return totoGame;
	}
	
	public List<LottoGame> getLottoGames()
	{
		return lottoGame;
	}
	
	public void addGame(LottoGame game)
	{
		lottoGame.add(game);
		games.put(idCounter++, game);
	}
	
	public void addGame(TotoGame game)
	{
		totoGame.add(game);
		games.put(idCounter++, game);
	}
	
	public Game getGame(long id)
	{
		return games.get(id);
	}
}
