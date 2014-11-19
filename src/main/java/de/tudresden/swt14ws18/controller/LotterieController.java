package de.tudresden.swt14ws18.controller;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

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
	public index(){
		retrun "/index";
	}
}
