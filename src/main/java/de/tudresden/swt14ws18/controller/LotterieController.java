package de.tudresden.swt14ws18.controller;

import java.util.Optional;

import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.useraccountmanager.CommunityRepository;
import de.tudresden.swt14ws18.useraccountmanager.CustomerRepository;

@Controller
public class LotterieController {
	
	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;
	private final CommunityRepository communityRepository;
	private final AuthenticationManager authenticationManager;
	
	@Autowired
	public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository, AuthenticationManager authenticationManager){
		
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(communityRepository, "CommunityRepository must not be null");
		
		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
		this.communityRepository = communityRepository;
		this.authenticationManager = authenticationManager;
	}
	@RequestMapping("/")
	public String Toindex(){
		return "index";
	}
	
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	@RequestMapping("/gameoverview")
	public String gameoverview(){
		return "games/overview";
	}
	
	@RequestMapping("/toto")
	public String toto(){
		return "games/toto";
	}
	
	@RequestMapping("/lotto")
	public String lotto(){
		return "games/lotto";
	}
	
	@RequestMapping("/groupoverview")
	public String groupoverview(){
		return "groups/overview";
	}
	
	@RequestMapping("/groupcreate")
	public String groupcreate(){
		return "groups/create";
	}
	
	@RequestMapping("/groupjoin")
	public String groupjoin(){
		return "groups/join";
	}
	
	@RequestMapping("/groupmanage")
	public String groupmanage(){
		return "groups/manage";
	}
	@RequestMapping("/profil")
	public String profil(){
		return "profil";
	}
	
	@RequestMapping("/bankaccount")
	public String bankaccount(){
		return "bankaccount";
	}
	
	@RequestMapping("/logout")
	public String logout(){
		return "logout";
	}
	
//	@RequestMapping("/registration")
//	public String registration(){
//		return "register";
//	}
	
	@RequestMapping({"/registration"})
	public String register(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap modelMap){
		if(userAccountManager.contains(new UserAccountIdentifier(vorname))){
			modelMap.addAttribute("registrationError", true);
			return "registration";
		}
		userAccountManager.save(userAccountManager.create(vorname, passwort));
		return "index";
	}
	
	@RequestMapping({"/input"})
	public String input(@RequestParam("vorname") String vorname, @RequestParam("passwort") String passwort, ModelMap modelMap){
		Optional<UserAccount> user = userAccountManager.get(new UserAccountIdentifier(vorname));
		
		if(user.isPresent()){
			
		  if(authenticationManager.matches(new Password(passwort), user.get().getPassword())){
		      
			  
			  return "index";//index();
		  }
		  
		}
		
		modelMap.addAttribute("loginError",true);
		return "index";
	}
	
}
