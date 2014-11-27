package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
	    AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TipFactory tipFactory, TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo) {
	Assert.notNull(authenticationManager, "UserAccountManager must not be null!");
	Assert.notNull(bankAccountRepository, "UserAccountManager must not be null!");
	Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
	Assert.notNull(customerRepository, "CustomerRepository must not be null!");
	Assert.notNull(communityRepository, "CommunityRepository must not be null");

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
    public String toto(ModelMap map){
	List<Integer> dates = new ArrayList<>();
	dates.add(13);
	dates.add(14);
    map.addAttribute("games", dates);    
	handleGeneralValues(map);
	return "games/toto";
    }

    @RequestMapping("/totoTipp")
    public String totoTipp(@RequestParam("id") int id, ModelMap map) {
	handleGeneralValues(map);	
	map.addAttribute("matches", totoRepo.findByMatchDay(id));	
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
	    map.addAttribute("messages",customer.getMessageCount());
	    map.addAttribute("state", customer.getState());
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
