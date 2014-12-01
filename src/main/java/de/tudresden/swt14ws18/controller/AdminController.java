package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Controller
public class AdminController {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final CommunityRepository communityRepository;
    private final AuthenticationManager authenticationManager;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoRepo;
    private final LottoTipCollectionRepository lottoTipCollectionRepo;
    private final TotoTipCollectionRepository totoTipCollectionRepo;
    private final TipFactory tipFactory;
    private final TotoTipRepository totoTipRepository;
    private final BusinessTime time;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static final long MINUTES_BEFORE_DATE = 5;

    @Autowired
    public AdminController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
	    AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TipFactory tipFactory,
	    TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo,
	    BusinessTime time, TotoTipRepository totoTipRepository) {
	Assert.notNull(authenticationManager, "UserAccountManager must not be null!");
	Assert.notNull(bankAccountRepository, "UserAccountManager must not be null!");
	Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
	Assert.notNull(customerRepository, "CustomerRepository must not be null!");
	Assert.notNull(communityRepository, "CommunityRepository must not be null");

	this.time = time;
	this.totoRepo = totoRepo;
	this.tipFactory = tipFactory;
	this.userAccountManager = userAccountManager;
	this.customerRepository = customerRepository;
	this.communityRepository = communityRepository;
	this.bankAccountRepository = bankAccountRepository;
	this.authenticationManager = authenticationManager;
	this.lottoTipCollectionRepo = lottoTipCollectionRepo;
	this.totoTipCollectionRepo = totoTipCollectionRepo;
	this.totoTipRepository = totoTipRepository;

    }

    @RequestMapping("/placedtotobets")
    public String placedtotobets(ModelMap map) {

	handleGeneralValues(map);
	//
	// Map<TotoMatch, Integer> anzahltipps= new HashMap<>();
	// for(TotoTip totoTip : totoTipRepository.findAll()){
	// if(anzahltipps.containsKey(totoTip.getGame())){
	// int i = anzahltipps.get(totoTip.getGame());
	// i++;
	// anzahltipps.put(totoTip.getGame(), i);
	// }
	// else{anzahltipps.put(totoTip.getGame(), 1);}
	// }
	//
	map.addAttribute("tips", totoTipRepository.findAll());

	return "statistics/placedtotobets";
    }

    public void handleGeneralValues(ModelMap map) {
	if (authenticationManager.getCurrentUser().isPresent()) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    map.addAttribute("balance", new DecimalFormat("#0.00").format(customer.getAccount().getBalance()));

	}

    }

}
