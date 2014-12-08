package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.Lotterie;
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
    protected static final long MINUTES_BEFORE_DATE = 5;
    protected static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#0.00");

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
        map.addAttribute("time", Lotterie.getInstance().getTime().getTime().format(Lotterie.OUTPUT_DTF));

        if (authenticationManager.getCurrentUser().isPresent()) {
            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
            map.addAttribute("balance", MONEY_FORMAT.format(customer.getAccount().getBalance()));
        }
    }
}
