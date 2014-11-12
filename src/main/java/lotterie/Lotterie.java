package lotterie;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, Lotterie.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, Lotterie.class })
@ComponentScan
public class Lotterie {
	
	public static void main(String[] args) {
		SpringApplication.run(Lotterie.class, args);
	}
	
	@Configuration
	static class LotterieWebConfiguration extends SalespointWebConfiguration {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/Index").setViewName("index");
		}
	}

}
