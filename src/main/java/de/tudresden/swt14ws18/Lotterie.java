package de.tudresden.swt14ws18;

import java.time.format.DateTimeFormatter;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.salespointframework.time.BusinessTime;
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
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;

/*
 * TODO Gruppenfunktionalität
 * TODO Wettübersicht des Admins muss funktionieren + schön aussehen
 * TODO Statisik des Admins ausbauen (Transaktionen zusammensuchen und auswerten)
 * TODO Zeitanzeige + Zeitmanipulationsbuttons
 * TODO Tippscheine müssen anklickbar und anzeigbar sein
 *      
 * TODO Lottotippabgabe hübsch gestalten (optional)
 * TODO Admin sollte keine Tipps abgeben dürfen
 * TODO Mitteilungsbildschirm überarbeiten (Mitteilungen bezahlbar)
 * TODO Statusmeldungen
 * TODO Uhrzeiten bei Fußballspielen anzeigen
 * TODO bei Spielen Datumsanzeige: Wann findet die nächste Ziehung/das nächste Spiel statt?
 *      
 *      
 * @author Reinhard_2
 *
 */

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, Lotterie.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, Lotterie.class })
@ComponentScan
public class Lotterie {
    /**
     * Standard Datumsformat für die allgemeine Zeitausgabe.
     */
    public static final DateTimeFormatter OUTPUT_DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static final DateTimeFormatter OUTPUT_DTF_LARS = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private static final double LOTTO_PRICE = 1.00D;
    private static final double INPUT_INTO_POT = 0.9D;
    
    private static Lotterie instance;
    private BusinessTime time;
    private TransactionRepository transactionRepo;
    private LottoMatchRepository lottoRepo;
    private CustomerRepository customerRepository;
    private MessageRepository messageRepo;
    private UserAccountManager userAccountManager;
    private BankAccountRepository bankAccountRepo;

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
    public void setCustomerRepository(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
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

    public BusinessTime getTime() {
	return time;
    }

    public static Lotterie getInstance() {
	return instance;
    }

    public static void main(String[] args) {
	SpringApplication.run(Lotterie.class, args);
    }

    public BankAccount getBankAccount() {
	return customerRepository.findByUserAccount(userAccountManager.get(new UserAccountIdentifier("admin")).get()).getAccount();
    }

    public void setNextLottoPot(LottoGame game) {
	LottoGame result = lottoRepo.findByResultOrderByDateAsc(null).get(0);

	if (result != null)
	    result.setWinningPot(game.getRemainingPot() + game.countObservers() * LOTTO_PRICE * INPUT_INTO_POT);
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
    
    public CustomerRepository getCustomerRepository(){
        return customerRepository;
    }

    public MessageRepository getMessagesRepository() {
        return messageRepo;
    }
    
    public BankAccountRepository getBankAccountRepository() {
        return bankAccountRepo;
    }
}
