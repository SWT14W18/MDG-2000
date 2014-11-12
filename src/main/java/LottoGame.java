
public class LottoGame extends Game {

	private String test;
	
	public LottoGame(String str)
	{
		this.test = str;
	}
	
	
	@Override
	public String getTitle() {
		return test;
	}


	@Override
	public String getID() {
		return "/index/"+test;
	}

}
