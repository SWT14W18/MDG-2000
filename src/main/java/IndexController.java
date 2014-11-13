import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController
{
    @RequestMapping("/index")
    public String index(ModelMap modelMap)
    {
        return "index";
    }
    
    @RequestMapping("/games/lotto")
    public String gamesLotto(ModelMap modelMap)
    {
        modelMap.addAttribute("text", Prototype.getInstance().getGameManager().getLottoGames());
        
        return "games";
    }
    
    @RequestMapping("/games/toto")
    public String gamesToto(ModelMap modelMap)
    {
        modelMap.addAttribute("text", Prototype.getInstance().getGameManager().getTotoGames());
        
        return "games";
    }
    
    @RequestMapping("/game")
    public String gameToto(@RequestParam("id") long id, ModelMap modelMap)
    {
        Game game = Prototype.getInstance().getGame(id);
        modelMap.addAttribute("gameId", id);
        
        if (game instanceof TotoGame)
            return handleTotoGame((TotoGame) game, modelMap);
        else if (game instanceof LottoGame)
            return handleLottoGame((LottoGame) game, modelMap);
        
        return "error";
    }
    
    @RequestMapping("/game/createTip")
    public String createTip(@RequestParam Map<String, String> params, ModelMap modelMap)
    {
        Game game = Prototype.getInstance().getGame(Long.valueOf(params.get("gameid")));
        
        if (game instanceof TotoGame)
            return createTotoTip((TotoGame) game, params, modelMap);
        else if (game instanceof LottoGame)
            return createLottoTip((LottoGame) game, params, modelMap);
        
        return "error";
    }
    
    @RequestMapping("/tips")
    public String getTips(ModelMap modelMap)
    {
        modelMap.addAttribute("tips", Prototype.getInstance().getTips());
        
        return "tips";
    }
    
    private String createLottoTip(LottoGame game, Map<String, String> params, ModelMap modelMap)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private String createTotoTip(TotoGame game, Map<String, String> params, ModelMap modelMap)
    {
        Map<Long, TotoValue> tips = new HashMap<>();
        for (TotoMatch match : game.getMatches())
        {
            if (params.containsKey(String.valueOf(match.getID())))
                continue;
            
            try
            {
                tips.put(match.getID(), TotoValue.getByValue(params.get(String.valueOf(match.getID()))));
            }
            catch (NumberFormatException e)
            {
                continue;
            }
        }
        
        Prototype.getInstance().addTip(new TotoTip(game, tips));
        
        return "success";
    }
    
    private String handleLottoGame(LottoGame game, ModelMap modelMap)
    {
        // TODO Auto-generated method stub
        return "lotto";
    }
    
    private String handleTotoGame(TotoGame game, ModelMap modelMap)
    {
        
        modelMap.addAttribute("matches", game.getMatches());
        return "toto";
    }
}
