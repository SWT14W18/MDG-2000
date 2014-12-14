package de.tudresden.swt14ws18.controller;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
@PreAuthorize("hasRole('ROLE_ANONYM')")
public class AnonymController extends ControllerBase {

    @Autowired
    public AnonymController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    @RequestMapping("/anonymAuszahlen")
    public String auszahlen(ModelMap map) {
        handleGeneralValues(map);
        
        if (authenticationManager.getCurrentUser().isPresent()) {
            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
            if (!customer.getAccount().outgoingTransaction(null, customer.getAccount().getBalance(), "Auszahlung") && !(customer.getAccount().getBalance() > 0)) {
                map.addAttribute("paymentOutAnonymError", true);
                return "forward:bankaccount";
            }
        }
        map.addAttribute("paymentOutAnonymSuccess", true);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
        customer.setStatus(Status.CLOSED);
        authenticationManager.getCurrentUser().get().remove(Constants.CUSTOMER);
        authenticationManager.getCurrentUser().get().remove(Constants.ANONYM);
        customerRepository.save(customer);
        // TODO Meldung wird bei logout nicht übermittelt, Funktion nicht hier ausführen.
        return "redirect:logout";
    }

    @RequestMapping("/anonymbankaccount")
    public String bankaccount(ModelMap map) {

        handleGeneralValues(map);
        return "anonym/anonymbankaccount";
    }

}
