package de.tudresden.swt14ws18.controller;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.LottoTipRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

@Controller
public class LotterieController extends ControllerBase {
    @Autowired
    public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    @RequestMapping({ "/", "/index" })
    public String Toindex(ModelMap map) {
        handleGeneralValues(map);
        return "index";
    }

    @RequestMapping("/login")
    public String login(ModelMap map) {

        handleGeneralValues(map);
        return "index";
    }

    @RequestMapping("/impressum")
    public String getImpressum(ModelMap map) {
        handleGeneralValues(map);
        return "impressum";
    }

    @RequestMapping("/logout")
    public String logout(ModelMap map) {

        handleGeneralValues(map);
        return "logout";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String reg(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap map) {

        handleGeneralValues(map);

        if (vorname.isEmpty()) {
            map.addAttribute("registrationError", true);
            return "forward:registration";
        }
        if (passwort.isEmpty()) {
            map.addAttribute("registrationError", true);
            return "forward:registration";
        }

        if (userAccountManager.contains(new UserAccountIdentifier(vorname))) {
            map.addAttribute("registrationError", true);
            return "forward:registration";
        }

        createUser(vorname, passwort);
        map.addAttribute("registrationSuccess", true);
        return "forward:index";
    }

    private void createUser(String name, String password) {
        UserAccount ua = userAccountManager.create(name, password, Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(ua);

        BankAccount ba = new BankAccount();

        ConcreteCustomer c1 = new ConcreteCustomer(name, Status.ACTIVE, ua, ba);

        bankAccountRepository.save(ba);
        customerRepository.save(c1);
    }

    @RequestMapping("/registration")
    public String registration(ModelMap map) {

        handleGeneralValues(map);
        return "registration";
    }

}
