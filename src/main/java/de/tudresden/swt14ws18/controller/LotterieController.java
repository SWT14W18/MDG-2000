package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.BankAccountRepository;
import de.tudresden.swt14ws18.gamemanagement.GameManager;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.CommunityRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.CustomerRepository;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Controller
public class LotterieController {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final CommunityRepository communityRepository;
    private final AuthenticationManager authenticationManager;
    private final BankAccountRepository bankAccountRepository;
    private final GameManager gameManager;

    @Autowired
    public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
	    AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, GameManager gameManager) {
	Assert.notNull(gameManager, "UserAccountManager must not be null!");
	Assert.notNull(authenticationManager, "UserAccountManager must not be null!");
	Assert.notNull(bankAccountRepository, "UserAccountManager must not be null!");
	Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
	Assert.notNull(customerRepository, "CustomerRepository must not be null!");
	Assert.notNull(communityRepository, "CommunityRepository must not be null");

	this.gameManager = gameManager;
	this.userAccountManager = userAccountManager;
	this.customerRepository = customerRepository;
	this.communityRepository = communityRepository;
	this.bankAccountRepository = bankAccountRepository;
	this.authenticationManager = authenticationManager;
    }

    public void handleGeneralValues(ModelMap map) {
	if (authenticationManager.getCurrentUser().isPresent()) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    map.addAttribute("balance", new DecimalFormat("#.00").format(customer.getAccount().getBalance()));

	}

    }
    
    @RequestMapping("/einzahlen")
    public String einzahlen(@RequestParam("newMoney") double money, ModelMap map)
    {
	handleGeneralValues(map);

	if (authenticationManager.getCurrentUser().isPresent()) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
	    customer.getAccount().payIn(money);
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
	return "games/overview";
    }

    @RequestMapping("/toto")
    public String toto(ModelMap map) {
	map.addAttribute("games", gameManager.getUnfinishedGames(GameType.TOTO));

	handleGeneralValues(map);
	return "games/toto";
    }

    @RequestMapping("/lotto")
    public String lotto(ModelMap map) {

	handleGeneralValues(map);
	return "games/lotto";
    }

    @RequestMapping("/createLottoTip")
    public String createLottoTip(@RequestParam Map<String, String> params, ModelMap map) {

	handleGeneralValues(map);
	TipFactory.craftTips(params, customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get()));

	return "index";
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

    @RequestMapping({ "/reg"})
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
