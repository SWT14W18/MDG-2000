package de.tudresden.swt14ws18;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Initialisiert alle Daten beim Start des Programms.
 */
@Component
public class LotterieDataInitializer implements DataInitializer {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoMatchRepository;
    private final LottoMatchRepository lottoMatchRepository;
    private final MessageRepository messageRepository;
    private final CommunityRepository communityRepository;
    private final BusinessTime time;
    private static final LocalDateTime DEBUG_DATE = LocalDateTime.of(2014, 10, 1, 12, 0);

    @Autowired
    public LotterieDataInitializer(CommunityRepository communityRepository, CustomerRepository customerRepository,
            UserAccountManager userAccountManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoMatchRepository,
            LottoMatchRepository lottoMatchRepository, MessageRepository messageRepository, BusinessTime time) {

        Assert.notNull(customerRepository, "CustomerRepository must not be null!");
        Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

        this.communityRepository = communityRepository;
        this.messageRepository = messageRepository;
        this.customerRepository = customerRepository;
        this.userAccountManager = userAccountManager;
        this.bankAccountRepository = bankAccountRepository;
        this.totoMatchRepository = totoMatchRepository;
        this.lottoMatchRepository = lottoMatchRepository;
        this.time = time;
    }

    @Override
    public void initialize() {

        if (Lotterie.DEBUG) {
            Duration dur = Duration.between(time.getTime(), DEBUG_DATE);
            time.forward(dur);
        }

        initializeUsers(userAccountManager, customerRepository, bankAccountRepository);
        initializeData(lottoMatchRepository, totoMatchRepository);
    }

    private void initializeData(LottoMatchRepository lottoMatchRepository, TotoMatchRepository totoMatchRepository) {

        if (!lottoMatchRepository.findAll().iterator().hasNext()) {
            LocalDateTime ldt = time.getTime().withHour(12).withMinute(0).withSecond(0);
            ldt = ldt.with(DayOfWeek.SUNDAY);

            if (ldt.isBefore(time.getTime()))
                ldt.plusDays(7);

            for (int i = 0; i < 250; i++) {
                LottoGame game = new LottoGame(ldt);

                if (i == 0)
                    game.setWinningPot(100000);

                lottoMatchRepository.save(game);
                ldt = ldt.plusDays(7);
            }
        }

        if (totoMatchRepository.findAll().iterator().hasNext()) {
            return;
        }

        TotoDataInitializer totoDataInitializer = new TotoDataInitializer(this.totoMatchRepository);
        totoDataInitializer.totoInitialize();

    }

    private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository) {

        // damit wir nicht immer den db ordner löschen müssen
        if (userAccountManager.get(new UserAccountIdentifier(Constants.ADMIN_NAME)).isPresent()) {
            return;
        }

        UserAccount admin = userAccountManager.create(Constants.ADMIN_NAME, "123", Constants.ADMIN, Constants.USER, Constants.TOTO_LIST);
        userAccountManager.save(admin);

        BankAccount adminAccount = new BankAccount();
        ConcreteCustomer adminCustomer = new ConcreteCustomer(Constants.ADMIN_NAME, Status.ACTIVE, admin, adminAccount);

        adminAccount.payIn(1000000);

        bankAccountRepository.save(adminAccount);
        customerRepository.save(adminCustomer);

        UserAccount ua1 = userAccountManager.create("hans", "123", Constants.USER, Constants.CUSTOMER, Constants.CUSTOMER_BLOCKABLE,
                Constants.TOTO_LIST);
        userAccountManager.save(ua1);

        BankAccount ba1 = new BankAccount();
        ba1.payIn(100);

        ConcreteCustomer c1 = new ConcreteCustomer("hans", Status.ACTIVE, ua1, ba1);

        bankAccountRepository.save(Arrays.asList(ba1));
        customerRepository.save(Arrays.asList(c1));

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
