package de.tudresden.swt14ws18.controller;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tudresden.swt14ws18.useraccountmanager.CommunityRepository;
import de.tudresden.swt14ws18.useraccountmanager.CustomerRepository;

@Controller
public class LotterieController {
	
	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;
	private final CommunityRepository communityRepository;
	
	@Autowired
	public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository){
		
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(communityRepository, "CommunityRepository must not be null");
		
		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
		this.communityRepository = communityRepository;
	}

	@RequestMapping("/")
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
	
	@RequestMapping("/registration")
	public String registration(){
		return "registration";
	}
	
}
