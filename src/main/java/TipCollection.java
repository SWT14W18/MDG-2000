import java.util.List;


public class TipCollection<T extends Game> {
	private T game;
	private List<Tip> tips;
	
	public TipCollection(T game)
	{
		this.game = game;
	}
}
