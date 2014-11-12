
public class TotoMatch {
	private static long idCounter = 0;
	private String home;
	private String out;
	private long id;
	
	public TotoMatch(String home, String out)
	{
		this.home = home;
		this.out = out;
		id = idCounter++;
	}
	
	public String getHomeName()
	{
		return home;
	}
	
	public String getGuestName()
	{
		return out;
	}
	
	public long getID()
	{
		return id;
	}
}
