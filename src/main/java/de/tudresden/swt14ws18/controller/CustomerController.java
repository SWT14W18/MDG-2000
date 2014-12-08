package de.tudresden.swt14ws18.controller;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import de.tudresden.swt14ws18.useraccountmanager.Message;

@Controller
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public class CustomerController extends ControllerBase{

    @Autowired
    public CustomerController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo, lottoTipCollectionRepo,
                totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository, totoMatchRepository, messageRepo, tipFactory,
                transactionRepo);
        // TODO Auto-generated constructor stub
    }

    @RequestMapping("/payMessage")
    public String payMessage(@RequestParam("id") long id, ModelMap map) {
        Message message = messageRepo.findById(id);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

        if (customer.hasMessage(message))
            customer.payOneMessage(message);

        return "forward:profil";
    }
}
