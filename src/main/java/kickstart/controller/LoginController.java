package kickstart.controller;




import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
//	@RequestMapping("/login")
//	public String index() {
//		return "login";
//	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String addEntry(@RequestParam("user") String name, @RequestParam("pass") String pass, ModelMap model) {
		model.addAttribute("loginError",true);
		return "login";
	}
}
