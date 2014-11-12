import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

	private Random random = new Random();
	
	@RequestMapping("/index")
	public String index(ModelMap modelMap)
	{
		modelMap.addAttribute("text", new LottoGame[]{new LottoGame("Das"), new LottoGame("ist"), new LottoGame("ein"), new LottoGame("test") });//"Das ist ein test text. Random Number: " + random.nextInt());
		
		return "index";
	}
	
	@RequestMapping("/games/lotto")
	public String gamesLotto(ModelMap modelMap)
	{
		modelMap.addAttribute("text", new LottoGame[]{new LottoGame("Das"), new LottoGame("ist"), new LottoGame("ein"), new LottoGame("test") });//"Das ist ein test text. Random Number: " + random.nextInt());
		
		return "games";
	}
	
	@RequestMapping("/games/toto")
	public String gamesToto(ModelMap modelMap)
	{
		modelMap.addAttribute("text", new TotoGame[]{new TotoGame(), new TotoGame(), new TotoGame(), new TotoGame() });

		return "games";
	}
	
	@RequestMapping("/game")
	public String gameToto(@RequestParam("id") long id, ModelMap modelMap)
	{
		
		modelMap.addAttribute("matches", new TotoGame().getMatches());

		return "toto";
	}
}
