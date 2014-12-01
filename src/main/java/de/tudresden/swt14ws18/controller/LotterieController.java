package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.tips.TipCollection;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Controller
public class LotterieController {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final CommunityRepository communityRepository;
    private final AuthenticationManager authenticationManager;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoRepo;
    private final LottoTipCollectionRepository lottoTipCollectionRepo;
    private final TotoTipCollectionRepository totoTipCollectionRepo;
    private final TipFactory tipFactory;
    private final BusinessTime time;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static final long MINUTES_BEFORE_DATE = 5;

    @Autowired
    public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
	    AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TipFactory tipFactory,
	    TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo,
	    BusinessTime time) {
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
    }

    public void handleGeneralValues(ModelMap map) {
	if (authenticationManager.getCurrentUser().isPresent()) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    map.addAttribute("balance", new DecimalFormat("#0.00").format(customer.getAccount().getBalance()));

	}

    }

    @RequestMapping("/einzahlen")
    public String einzahlen(@RequestParam("newMoney") double money, ModelMap map) {
	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent() && money > 0) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
	    customer.getAccount().payIn(money);
	    bankAccountRepository.save(customer.getAccount());
	}
	return "redirect:index";
    }

    @RequestMapping("/auszahlen")
    public String auszahlen(@RequestParam("newMoney") double money, ModelMap map) {
	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent() && money > 0) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
	    customer.getAccount().outgoingTransaction(null, money);
	    bankAccountRepository.save(customer.getAccount());
	}
	return "redirect:index";
    }

    @RequestMapping({ "/", "/index" })
    public String Toindex(ModelMap map) {
	handleGeneralValues(map);
	return "index";
    }

    @RequestMapping("/statisticsoverview")
    public String statisticsoverview(ModelMap map) {
	handleGeneralValues(map);

	ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
	map.addAttribute("transactions", customer.getAccount().getTransactions());

	return "statistics/overview";
    }

    @RequestMapping("/gameoverview")
    public String gameoverview(ModelMap map) {

	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent()) {

	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    map.addAttribute("tips", getTips(customer));
	}
	return "games/overview";
    }

    private List<TipCollection<?>> getTips(ConcreteCustomer customer) {
	List<TipCollection<?>> tips = new ArrayList<>();

	tips.addAll(lottoTipCollectionRepo.findByOwner(customer));
	tips.addAll(totoTipCollectionRepo.findByOwner(customer));

	return tips;
    }


    
    @RequestMapping("/toto")
    public String toto(ModelMap map) {
    	
    	map.addAttribute("totoGameTypes", TotoGameType.values());
//	handleGeneralValues(map);
//
//	Map<TotoGameType, Set<Integer>> dates = getRemainingMatchDates();
//
//	map.addAttribute("games", dates.entrySet());
	return "games/toto";
    }

    private Map<TotoGameType, Set<Integer>> getRemainingMatchDates() {
	Map<TotoGameType, Set<Integer>> list = new HashMap<>();

	for (TotoMatch match : totoRepo.findByTotoResult(TotoResult.NOT_PLAYED)) {

	    LocalDateTime localTime = time.getTime();
	    LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);
	    if (!localTime.isAfter(date)) {
		if (!list.containsKey(match.getTotoGameType()))
		    list.put(match.getTotoGameType(), new TreeSet<Integer>());

		list.get(match.getTotoGameType()).add(match.getMatchDay());
	    }

	}

	return list;
    }
    
    @RequestMapping("/totoMatchDays")
    public String totoMatchDays(@RequestParam("id") String totoGameTypeString, ModelMap modelMap){
    	
    	TotoGameType totoGameType = null;
    	switch(totoGameTypeString){
    	case "DFB Pokal":totoGameType = TotoGameType.POKAL;
    	case "1. Bundesliga":totoGameType = TotoGameType.BUNDESLIGA1;
    	case "2. Bundesliga":totoGameType = TotoGameType.BUNDESLIGA2;
    	}
    	Set<Integer> set = new TreeSet<>();
    	for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
    	    LocalDateTime localTime = time.getTime();
    	    LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);
    	    if (!localTime.isAfter(date)) {
    	    	set.add(match.getMatchDay());
    	    }
    	}
    	modelMap.addAttribute("matchDays", set);
    	modelMap.addAttribute("totoGameType", totoGameType);
    	return "games/totoMatchDays";
    }

    @RequestMapping("/totoTipp")
    public String totoTipp(@RequestParam("id") int id, ModelMap map) {
	handleGeneralValues(map);

	List<TotoMatch> list = new ArrayList<>();

	for (TotoMatch match : totoRepo.findByMatchDay(id)) {
	    if (match.isFinished())
		continue;

	    LocalDateTime localTime = time.getTime();
	    LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);

	    if (!localTime.isAfter(date)) {
		list.add(match);
	    }
	}

	map.addAttribute("matches", list);
	return "games/totoTipp";
    }

    @RequestMapping("/lotto")
    public String lotto(ModelMap map) {

	handleGeneralValues(map);
	return "games/lotto";
    }

    @RequestMapping("/createLottoTip")
    public String createLottoTip(@RequestParam Map<String, String> params, ModelMap map) {

	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent()) {

	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    tipFactory.craftLottoTips(params, customer);
	}
	return "index";
    }

    @RequestMapping(value = "/createTotoTip", method = RequestMethod.POST)
    public String createTotoTip(@RequestParam Map<String, String> params, ModelMap map) {

	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent()) {

	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    tipFactory.craftTotoTips(params, customer);
	}
	return "index";
    }

    @RequestMapping("/impressum")
    public String getImpressum(ModelMap map) {
	handleGeneralValues(map);
	return "impressum";
    }

    @RequestMapping("/groupoverview")
    public String groupoverview(ModelMap map) {

	handleGeneralValues(map);
	return "groups/overview";
    }

    @RequestMapping("/groupcreate")
    public String groupcreate(ModelMap map) {

	handleGeneralValues(map);
	return "groups/create";
    }

    @RequestMapping("/groupjoin")
    public String groupjoin(ModelMap map) {

	handleGeneralValues(map);
	return "groups/join";
    }

    @RequestMapping("/groupmanage")
    public String groupmanage(ModelMap map) {

	handleGeneralValues(map);
	return "groups/manage";
    }

    @RequestMapping("/profil")
    public String profil(ModelMap map) {

	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent()) {

	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
	    map.addAttribute("name", customer.getName());
	    map.addAttribute("messages", customer.getMessageCount());
	    map.addAttribute("state", customer.getState());
	    map.addAttribute("messageList", customer.getMessages());
	}
	return "profil";
    }

    @RequestMapping("/bankaccount")
    public String bankaccount(ModelMap map) {

	handleGeneralValues(map);
	return "bankaccount";
    }

    @RequestMapping("/logout")
    public String logout(ModelMap map) {

	handleGeneralValues(map);
	return "logout";
    }

    @RequestMapping({ "/reg" })
    public String reg(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap map) {

	handleGeneralValues(map);
	if (userAccountManager.contains(new UserAccountIdentifier(vorname))) {
	    map.addAttribute("registrationError", true);
	    return "registration";
	}

	final Role customerRole = new Role("ROLE_USER");

	UserAccount ua = userAccountManager.create(vorname, passwort, customerRole);
	userAccountManager.save(ua);

	BankAccount ba = new BankAccount();

	ConcreteCustomer c1 = new ConcreteCustomer(vorname, passwort, Status.ACTIVE, ua, ba);

	bankAccountRepository.save(ba);
	customerRepository.save(c1);
	return "index";
    }

    @RequestMapping({ "/registration" })
    public String registration(ModelMap map) {

	handleGeneralValues(map);
	return "registration";
    }

    @RequestMapping({ "/input" })
    public String input(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap map) {

	handleGeneralValues(map);
	Optional<UserAccount> user = userAccountManager.get(new UserAccountIdentifier(vorname));

	if (user.isPresent()) {

	    if (authenticationManager.matches(new Password(passwort), user.get().getPassword())) {

		return "index";// index();
	    }

	}

	map.addAttribute("loginError", true);
	return "index";
    }

}
