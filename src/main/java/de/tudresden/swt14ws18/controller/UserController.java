package de.tudresden.swt14ws18.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
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
import de.tudresden.swt14ws18.tips.Tip;
import de.tudresden.swt14ws18.tips.TipCollection;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController extends ControllerBase {

    @Autowired
    public UserController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    @RequestMapping("/einzahlen")
    public String einzahlen(@RequestParam("newMoney") String moneyString, ModelMap map) {
        handleGeneralValues(map);

        try {
            double money = Double.parseDouble(moneyString);
            if (authenticationManager.getCurrentUser().isPresent() && money > 0) {
                ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
                customer.getAccount().payIn(money);
            }
        } catch (Exception e) {
            map.addAttribute("paymentInError", true);
            return "forward:bankaccount";
        }
        map.addAttribute("paymentInSuccess", true);
        return "forward:bankaccount";
    }

    @RequestMapping("/auszahlen")
    public String auszahlen(@RequestParam("newMoney") String moneyString, ModelMap map) {
        handleGeneralValues(map);

        try {
            double money = Double.parseDouble(moneyString);
            if (authenticationManager.getCurrentUser().isPresent() && money > 0) {
                ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
                if (!customer.getAccount().outgoingTransaction(null, money)) {
                    map.addAttribute("paymentOutError", true);
                    return "forward:bankaccount";
                }
            }
        } catch (Exception e) {
            map.addAttribute("paymentOutError", true);
            return "forward:bankaccount";
        }
        map.addAttribute("paymentOutSuccess", true);
        return "forward:bankaccount";
    }

    @RequestMapping("/bankaccount")
    public String bankaccount(ModelMap map) {

        handleGeneralValues(map);
        return "bankaccount";
    }
}
