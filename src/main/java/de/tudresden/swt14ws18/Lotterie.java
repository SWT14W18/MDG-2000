package de.tudresden.swt14ws18;

import java.util.List;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.LottoTipRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.util.Constants;

/*
 * TODO Statisik des Admins ausbauen (Transaktionen zusammensuchen und auswerten)
 * TODO Anonymen User ( 80% FERTIG , Rechte entzug und Notification Box )
 * TODO KUNDENWUNSCH!!
 * TODO arc 42 Entwicklerdokumentation		
 */

/*
 * Entwurf Rollensystem:
 * Gast - keine Rolle, unangemeldeter Nutzer (login, register, impressum usw.)
 * USER - registrierter Benutzer (logout, einzahlen, etc.)
 * BOSS - Admin Funktionen
 * CUSTOMER - Kunden Funktionen die immer vorhanden sind (Messages bezahlen, Einzahlen, Auszahlen, etc.)
 * CUSTOMER_BLOCKABLE - Kunden funktionen die gesperrt werden können
 * ANONYM - Anonymes Konto für Mitarbeiter die Tippscheine "echter" Kunden dem System hinzufügen, Kann 1 Tippschein erstellen, Auszahlen und der Account wird auf CLOSED gesetzt
 */

/**
 * Die Hauptklasse des Projekts. Hier sind jede Menge getter und wenig Implementierung.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, Lotterie.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, Lotterie.class })
@ComponentScan
public class Lotterie {

    public static boolean DEBUG = false;

    private static Lotterie instance;
    private BusinessTime time;
    private TransactionRepository transactionRepo;
    private LottoMatchRepository lottoRepo;
    private CustomerRepository customerRepository;
    private MessageRepository messageRepo;
    private UserAccountManager userAccountManager;
    private BankAccountRepository bankAccountRepo;
    private LottoTipRepository lottoTipRepo;
    private TotoTipRepository totoTipRepo;
    private LottoTipCollectionRepository lottoTipColRepo;
    private TotoTipCollectionRepository totoTipColRepo;
    private AuthenticationManager authManager;

    public Lotterie() {
        instance = this;
    }

    @Autowired
    public void setUserAccountManager(UserAccountManager userAccountManager) {
        this.userAccountManager = userAccountManager;
    }

    @Autowired
    public void setBankAccountRepository(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepo = bankAccountRepository;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setTotoTipRepository(TotoTipRepository totoTipRepo) {
        this.totoTipRepo = totoTipRepo;
    }

    @Autowired
    public void setLottoTipRepository(LottoTipRepository lottoTipRepo) {
        this.lottoTipRepo = lottoTipRepo;
    }

    @Autowired
    public void setTotoTipCollectionRepository(TotoTipCollectionRepository totoTipColRepo) {
        this.totoTipColRepo = totoTipColRepo;
    }

    @Autowired
    public void setLottoTipColectionRepository(LottoTipCollectionRepository lottoTipColRepo) {
        this.lottoTipColRepo = lottoTipColRepo;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Autowired
    public void setTime(BusinessTime time) {
        this.time = time;
    }

    @Autowired
    public void setLottoMatchRepository(LottoMatchRepository lottoRepo) {
        this.lottoRepo = lottoRepo;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public UserAccountManager getUserAccountManager() {
        return this.userAccountManager;
    }

    public AuthenticationManager getAuthenticationManager() {
        return this.authManager;
    }

    @Autowired
    public void setAuthenticatioManager(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public BusinessTime getTime() {
        return time;
    }

    public static Lotterie getInstance() {
        return instance;
    }

    /**
     * @param args
     *            Wenn das 1. Element "debug" ist, wird das Programm im Debug Modus gestartet.
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("debug"))
            DEBUG = true;

        if (DEBUG)
            System.out.println("Starting in Developer Mode - app date changed to 1.10.2014");

        SpringApplication.run(Lotterie.class, args);
    }

    public BankAccount getBankAccount() {
        return customerRepository.findByUserAccount(userAccountManager.get(new UserAccountIdentifier(Constants.ADMIN_NAME)).get()).getAccount();
    }

    /**
     * Setzt den Jackpot der nächsten unbearbeiteten Lottoziehung. Lottoziehungen werden dabei nach ihrem Ziehungsdatum sortiert.
     * 
     * Der Gewinntopf für die nächste Ziehung ist 90% der Summe aller Einsätze der vorherigen Ziehung plus die nicht ausgezahlten Gewinne der
     * vorherigen Ziehung
     * 
     * @param game
     *            Das letzte abgearbeitete Spiel, aus welchem der nicht ausgeschüttete Gewinn berechnet wird.
     */
    public void setNextLottoPot(LottoGame game) {
        List<LottoGame> l = lottoRepo.findByResultOrderByDateAsc(null);
        LottoGame result = l.get(0).equals(game) ? l.get(1) : l.get(0);

        int count = 0;
        for (LottoTip tip : getLottoTipRepository().findByLottoGame(game))
            if (tip.isValid())
                count++;

        result.setWinningPot((game.getRemainingPot() + (count * Constants.LOTTO_PRICE)) * Constants.INPUT_INTO_POT);
    }

    @Configuration
    static class LotterieWebConfiguration extends SalespointWebConfiguration {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/login").setViewName("login");
        }
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    static class WebSecurityConfiguration extends SalespointSecurityConfiguration {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginPage("/login").loginProcessingUrl("/login").and().logout()
                    .logoutUrl("/logout").logoutSuccessUrl("/");
        }
    }

    public TransactionRepository getTransactionRepo() {
        return transactionRepo;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public MessageRepository getMessagesRepository() {
        return messageRepo;
    }

    public BankAccountRepository getBankAccountRepository() {
        return bankAccountRepo;
    }

    public LottoTipRepository getLottoTipRepository() {
        return lottoTipRepo;
    }

    public TotoTipRepository getTotoTipRepository() {
        return totoTipRepo;
    }

    public LottoTipCollectionRepository getLottoTipCollectionRepository() {
        return lottoTipColRepo;
    }

    public TotoTipCollectionRepository getTotoTipCollectionRepository() {
        return totoTipColRepo;
    }
}
