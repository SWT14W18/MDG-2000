package de.tudresden.swt14ws18;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.LottoTipRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

@Component
public class LotterieDataInitializer implements DataInitializer {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoMatchRepository;
    private final LottoMatchRepository lottoMatchRepository;
    private final MessageRepository messageRepository;
    private final CommunityRepository communityRepository;
    private final LottoTipRepository lottoTipRepository;
    private final LottoTipCollectionRepository lottoTipCollectionRepository;

    @Autowired
    public LotterieDataInitializer(CommunityRepository communityRepository, CustomerRepository customerRepository, UserAccountManager userAccountManager,
            BankAccountRepository bankAccountRepository, TotoMatchRepository totoMatchRepository, LottoMatchRepository lottoMatchRepository,
            MessageRepository messageRepository, LottoTipRepository lottoTipRepository, LottoTipCollectionRepository lottoTipCollectionRepository) {

        Assert.notNull(customerRepository, "CustomerRepository must not be null!");
        Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

        this.communityRepository = communityRepository;
        this.messageRepository = messageRepository;
        this.customerRepository = customerRepository;
        this.userAccountManager = userAccountManager;
        this.bankAccountRepository = bankAccountRepository;
        this.totoMatchRepository = totoMatchRepository;
        this.lottoMatchRepository = lottoMatchRepository;
        this.lottoTipRepository = lottoTipRepository;
        this.lottoTipCollectionRepository = lottoTipCollectionRepository;
    }

    @Override
    public void initialize() {
        initializeUsers(userAccountManager, customerRepository, bankAccountRepository);
        initializeData(lottoMatchRepository, totoMatchRepository, lottoTipRepository, lottoTipCollectionRepository);
    }

    private void initializeData(LottoMatchRepository lottoMatchRepository, TotoMatchRepository totoMatchRepository,
            LottoTipRepository lottoTipRepository, LottoTipCollectionRepository lottoTipCollectionRepository) {

        // lottoMatchRepository.deleteAll();
        // lottoTipRepository.deleteAll();
        // lottoTipCollectionRepository.deleteAll();
//         totoMatchRepository.deleteAll();

        if (!lottoMatchRepository.findAll().iterator().hasNext()) {
            LocalDateTime ldt = LocalDateTime.of(2014, 12, 7, 12, 0);
            for (int i = 0; i < 50; i++) {
                lottoMatchRepository.save(new LottoGame(ldt));
                ldt = ldt.plusDays(7);
            }
        }

        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 7, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 7, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 7, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 7, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 7, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 14, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 14, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 28, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 28, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 28, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));
        lottoTipRepository.save(new LottoTip(lottoMatchRepository.findByDate(LocalDateTime.of(2014, 12, 28, 12, 0)), new LottoNumbers(1, 2, 3, 4, 5,
                6, 7)));

        if (totoMatchRepository.findAll().iterator().hasNext()) {
            return;
        }

        try {
            new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.BUNDESLIGA1);
            new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.BUNDESLIGA2);
            new TotoDataInitializer(this.totoMatchRepository).totoInitialize(TotoGameType.POKAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository) {

        // damit wir nicht immer den db ordner löschen müssen
        if (userAccountManager.get(new UserAccountIdentifier("admin")).isPresent()) {
            return;
        }

        UserAccount admin = userAccountManager.create("admin", "123", Constants.ADMIN, Constants.USER);
        userAccountManager.save(admin);

        BankAccount adminAccount = new BankAccount();
        ConcreteCustomer adminCustomer = new ConcreteCustomer("admin", Status.ACTIVE, admin, adminAccount);

        bankAccountRepository.save(adminAccount);
        customerRepository.save(adminCustomer);


        UserAccount ua1 = userAccountManager.create("hans", "123", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(ua1);
        UserAccount ua2 = userAccountManager.create("dextermorgan", "123", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(ua2);
        UserAccount ua3 = userAccountManager.create("earlhickey", "123", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(ua3);
        UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE);
        userAccountManager.save(ua4);

        BankAccount ba1 = new BankAccount();
        BankAccount ba2 = new BankAccount();
        BankAccount ba3 = new BankAccount();
        BankAccount ba4 = new BankAccount();
        ba1.payIn(100);

        ConcreteCustomer c1 = new ConcreteCustomer("hans", Status.ACTIVE, ua1, ba1);
        ConcreteCustomer c2 = new ConcreteCustomer("dextermorgan", Status.ACTIVE, ua2, ba2);
        ConcreteCustomer c3 = new ConcreteCustomer("earlhickey", Status.ACTIVE, ua3, ba3);
        ConcreteCustomer c4 = new ConcreteCustomer("mclovinfogell", Status.ACTIVE, ua4, ba4);

        bankAccountRepository.save(Arrays.asList(ba1, ba2, ba3, ba4));
        customerRepository.save(Arrays.asList(c1, c2, c3, c4));

        Community com = new Community("Test", "test", c1);
        communityRepository.save(com);
        
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.TOTO);
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.TOTO);
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.LOTTO);
        c1.addMessage(GameType.LOTTO);

        messageRepository.save(c1.getMessages());

    }
}
