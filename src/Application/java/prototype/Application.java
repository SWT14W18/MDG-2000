package Application.java.prototype;

import org.salespointframework.Salespoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prototype.persistence.Prototype;
import prototype.persistence.PrototypeRepository;

@Configuration
@Import({ SalespointWebConfiguration.class})
@EntityScan(basePackageClasses = {Salespoint.class, Application.class})
@ComponentScan
public class Application {
	private static Application instance;
	private Prototype prototype;
	private PrototypeRepository prototypeRepository;
	
	public Application() {
		instance = this;
	}

	@Autowired
	public void Prototype(Prototype prototype){
		this.prototype = prototype;
	}
	
	@Autowired
	public void PrototypeRepository(PrototypeRepository prototypeRepository){
		this.prototypeRepository = prototypeRepository;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginProcessingUrl("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
