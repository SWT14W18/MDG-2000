import java.util.ArrayList;
import java.util.List;

import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/*
 * Funktionen
 * Spiele eines types anzeigen
 * Spiel auswählen
 * Tipp erstellen
 * Tipps einsehen
 * 
 * 
 * Startseite
 * 2 Knöpfe zu den beiden Spielen
 * 
 * TOTO Seite
 * Liste aller Spiele - Format "XX. Spieltag" - Link zu Spieltag
 * 
 * Lotto Seite
 * Liste aller Spiele - Format "Losung vom DD.MM.JJ" - Link zu Losung
 * 
 * Lotto Losung
 * 
 * 
 * 
 * TOTO Spieltag
 * Liste aller Spiele des Tages
 * Select Ergebnis
 * Absenden
 * --> Tipp erstellen
 */

@Configuration
//@Import({ SalespointWebConfiguration.class })
//@EnableAutoConfiguration
@ComponentScan
public class Prototype {
    private static Prototype instance;
    
    
	private GameManager gameManager = new GameManager();
	private List<Tip> tips = new ArrayList<>();
	
	{
	    instance = this;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Prototype.class, args);
	}

	@Configuration
	static class VideoShopWebConfiguration extends SalespointWebConfiguration {
		/**
		 * We configure {@code /login} to be directly routed to the
		 * {@code login} template without any controller interaction.
		 *
		 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry)
		 */
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			//registry.addViewController("/index").setViewName("index");
		}
	}
	
	public static Prototype getInstance()
	{
	    return instance;
	}
	
	public void addTip(Tip tip)
	{
	    tips.add(tip);
	}

    public Game getGame(long id)
    {
        // TODO Auto-generated method stub
        return new TotoGame();
    }

    public List<Tip> getTips()
    {
        return tips;
    }
}

@Configuration
@EnableWebMvcSecurity
class WebSecuriryConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().anyRequest().permitAll();
	}
}