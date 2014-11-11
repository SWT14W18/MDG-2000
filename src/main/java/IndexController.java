import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {

	private Random random = new Random();
	
	@RequestMapping("/index")
	public String index(ModelMap modelMap)
	{
		modelMap.addAttribute("text", "Das ist ein test text. Random Number: " + random.nextInt());
		
		return "index";
	}
}
