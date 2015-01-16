package Application.java.prototype.controller;

import org.springframework.aop.aspectj.annotation.PrototypeAspectInstanceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

	@RequestMapping("/")
	public String index() {
		return "welcome";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    public String index(ModelMap map, @RequestParam("value") Double value) {

        handleGeneralValues(map);
        double value = Prototype.getCounter();
        return "welcome";
    }

    @RequestMapping("/")
    public String index(ModelMap map) {

        handleGeneralValues(map);
        return "welcome";
    }	
	
	
	
	
	
	
	
	
	
}
