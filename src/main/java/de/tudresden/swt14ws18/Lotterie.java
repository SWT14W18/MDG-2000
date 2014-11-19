package de.tudresden.swt14ws18;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.TransactionRepository;
import de.tudresden.swt14ws18.gamemanagement.GameManager;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, Lotterie.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, Lotterie.class })
@ComponentScan
public class Lotterie {
    private static Lotterie instance;
    private BankAccount account = new BankAccount();
    private TransactionRepository transactionRepo;
    private GameManager gameManager;

    public Lotterie() {
	instance = this;
    }
    
    @Autowired
    public void setGameManager(GameManager gameManager) {
	this.gameManager = gameManager;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepo) {
	this.transactionRepo = transactionRepo;
    }

    public GameManager getGameManager() {
	return gameManager;
    }

    public static Lotterie getInstance() {
	return instance;
    }

    public static void main(String[] args) {
	SpringApplication.run(Lotterie.class, args);
    }

    public BankAccount getBankAccount() {
	return account;
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
}
