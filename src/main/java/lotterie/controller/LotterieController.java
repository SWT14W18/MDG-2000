package lotterie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
class LotterieController {
	
	@RequestMapping({"/","/Index"})
    public String Index() {
		return "Index";
	}
}
