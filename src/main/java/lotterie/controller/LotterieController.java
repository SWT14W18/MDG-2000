package lotterie.controller;

import java.util.Optional;

import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
class LotterieController {
	
	private final UserAccountManager userAccountManager;
	private final AuthenticationManager authenticationManager;
	
	@Autowired
	public LotterieController(UserAccountManager userAccountManager, AuthenticationManager authenticationManager){
		this.userAccountManager = userAccountManager;
		this.authenticationManager = authenticationManager;
	}
	
	@RequestMapping({"/","/Index"})
    public String index() {
		return "Index";
	}
	
	@RequestMapping({"/Login"})
	public String login(ModelMap modelmap){
		modelmap.addAttribute("Kunde", "Ich bin Günther");
		return "Login";
	}
	
	@RequestMapping({"/Registrieren"})
	public String registrieren(){
		return "Registrieren";
	}
	
	@RequestMapping({"/Input"})
	public String input(@RequestParam("vorname") String vorname, @RequestParam("passwort") String passwort){
		Optional<UserAccount> user = userAccountManager.get(new UserAccountIdentifier(vorname));
		
		if(user.isPresent()){
			
		  if(authenticationManager.matches(new Password(passwort), user.get().getPassword())) return "Index";
		  
		}
		return "Registrieren";
	}
}
