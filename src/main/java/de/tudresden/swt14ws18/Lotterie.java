package de.tudresden.swt14ws18;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@Import({ SalespointWebConfiguration.class })
@ComponentScan
public class Lotterie {
	private static Lotterie instance;
	
	private List<Kunde> clients = new ArrayList<>();
	private BankAccount account;
	private Bank bank;
	private GameManager manager;
	
	public List<Kunde> getClients()
	{
		return clients;
	}
	
	public Bank getBank()
	{
		return bank;
	}
	
	public BankAccount getAccount()
	{
		return account;
	}
	
	public void registerNewUser(String name, String password)
	{
		
	}
	
	public GameManager getGameManager()
	{
		return manager;
	}
	
	
	
	
	
	
	public Lotterie()
	{
		instance = this;
	}
	
	public static Lotterie getInstance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		SpringApplication.run(Lotterie.class, args);
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration 
	{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
		}
	}
}
