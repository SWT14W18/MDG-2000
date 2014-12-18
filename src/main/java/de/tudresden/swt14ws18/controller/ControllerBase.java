package de.tudresden.swt14ws18.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
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
import de.tudresden.swt14ws18.util.Constants;

public abstract class ControllerBase {
    protected final UserAccountManager userAccountManager;
    protected final CustomerRepository customerRepository;
    protected final CommunityRepository communityRepository;
    protected final AuthenticationManager authenticationManager;
    protected final BankAccountRepository bankAccountRepository;
    protected final TotoMatchRepository totoRepo;
    protected final LottoTipCollectionRepository lottoTipCollectionRepo;
    protected final TotoTipCollectionRepository totoTipCollectionRepo;
    protected final TotoTipRepository totoTipRepository;
    protected final LottoTipRepository lottoTipRepository;
    protected final TotoMatchRepository totoMatchRepository;
    protected final MessageRepository messageRepo;
    protected final TransactionRepository transactionRepo;
    protected final TipFactory tipFactory;
    protected final BusinessTime time;
    protected final LottoMatchRepository lottoMatchRepository;
    protected static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    public ControllerBase(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {

        Assert.notNull(authenticationManager, "UserAccountManager must not be null!");
        Assert.notNull(bankAccountRepository, "UserAccountManager must not be null!");
        Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
        Assert.notNull(customerRepository, "CustomerRepository must not be null!");
        Assert.notNull(communityRepository, "CommunityRepository must not be null");

        this.messageRepo = messageRepo;
        this.tipFactory = tipFactory;
        this.transactionRepo = transactionRepo;
        this.time = time;
        this.totoRepo = totoRepo;
        this.userAccountManager = userAccountManager;
        this.customerRepository = customerRepository;
        this.communityRepository = communityRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.authenticationManager = authenticationManager;
        this.lottoTipCollectionRepo = lottoTipCollectionRepo;
        this.totoTipCollectionRepo = totoTipCollectionRepo;
        this.totoTipRepository = totoTipRepository;
        this.lottoMatchRepository = lottoMatchRepository;
        this.lottoTipRepository = lottoTipRepository;
        this.totoMatchRepository = totoMatchRepository;
    }

    public void handleGeneralValues(ModelMap map) {
        map.addAttribute("time", Lotterie.getInstance().getTime().getTime().format(Constants.OUTPUT_DTF));

        if (authenticationManager.getCurrentUser().isPresent()) {
            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
            map.addAttribute("balance", Constants.MONEY_FORMAT.format(customer.getAccount().getBalance()));
            // TODO Neue Nachrichten sollten angezeigt werden mit bootbox
            map.addAttribute("newMessages", customer.getNumberOfNewMessages());
        }
        
    }

    public ConcreteCustomer getCurrentUser() {
        return authenticationManager.getCurrentUser().isPresent() ? customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get()) : null;
    }
    
    public boolean timeCheck(LocalDateTime t) {
        return time.getTime().isAfter(t.minusMinutes(Constants.MINUTES_BEFORE_DATE));
    }
    
    public LottoNumbers parseInput(Map<String, String> input) {
        if (!input.containsKey("number1"))
            return null;
        if (!input.containsKey("number2"))
            return null;
        if (!input.containsKey("number3"))
            return null;
        if (!input.containsKey("number4"))
            return null;
        if (!input.containsKey("number5"))
            return null;
        if (!input.containsKey("number6"))
            return null;
        if (!input.containsKey("super"))
            return null;

        try {
            int n1 = Integer.parseInt(input.get("number1"));
            int n2 = Integer.parseInt(input.get("number2"));
            int n3 = Integer.parseInt(input.get("number3"));
            int n4 = Integer.parseInt(input.get("number4"));
            int n5 = Integer.parseInt(input.get("number5"));
            int n6 = Integer.parseInt(input.get("number6"));
            int nsuper = Integer.parseInt(input.get("super"));

            return new LottoNumbers(nsuper, n1, n2, n3, n4, n5, n6);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
