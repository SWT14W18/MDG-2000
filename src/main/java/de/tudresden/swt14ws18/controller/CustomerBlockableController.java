package de.tudresden.swt14ws18.controller;

import java.util.Map;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@Controller
@PreAuthorize("hasRole('ROLE_CUSTOMER_BLOCKABLE')")
public class CustomerBlockableController extends ControllerBase{

    @Autowired
    public CustomerBlockableController(UserAccountManager userAccountManager, CustomerRepository customerRepository,
            CommunityRepository communityRepository, AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository,
            TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo,
            BusinessTime time, TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo, lottoTipCollectionRepo,
                totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository, totoMatchRepository, messageRepo, tipFactory,
                transactionRepo);
    }

    @RequestMapping(value = "/createLottoTip", method = RequestMethod.POST)
    public String createLottoTip(@RequestParam Map<String, String> params, ModelMap map) {

        handleGeneralValues(map);
        if(params.isEmpty()){
            map.addAttribute("lottoError", true);
            return "forward:/lotto";
        }
        
        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

            if(!tipFactory.craftLottoTips(params, customer)){
                map.addAttribute("lottoError", true);
                return "forward:/lotto";
            }
            map.addAttribute("lottoSuccess", true);
        }
        return "forward:gameoverview";
    }

    @RequestMapping(value = "/createTotoTip", method = RequestMethod.POST)
    public String createTotoTip(@RequestParam Map<String, String> params, ModelMap map) {

        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

            tipFactory.craftTotoTips(params, customer);
            map.addAttribute("totoSuccess", true);
        }
        return "forward:gameoverview";
    }
}
