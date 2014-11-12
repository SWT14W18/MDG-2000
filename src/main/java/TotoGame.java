import java.util.Arrays;
import java.util.Collection;

public class TotoGame extends Game {

	public void createTip() {

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "xx. Spieltag";
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "1";
	}

	public Collection<TotoMatch> getMatches() {
		return Arrays.asList(new TotoMatch("Team1", "Team2"), new TotoMatch(
				"Team3", "Team4"), new TotoMatch("Team5", "Team6"),
				new TotoMatch("Team7", "Team8"));
	}
}
