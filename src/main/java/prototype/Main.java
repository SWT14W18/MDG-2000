package prototype;

import org.salespointframework.Salespoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import prototype.persistence.Prototype;
import prototype.persistence.PrototypeRepository;


@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, Main.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, Main.class })
@ComponentScan
public class Main {
    private static Main instance;
    private Prototype account = new Prototype();
    private PrototypeRepository prototypeRepo;

    public Main() {
	instance = this;
    }

    @Autowired
    public void setPrototypeRepository(PrototypeRepository prototypeRepo) {
	this.prototypeRepo = prototypeRepo;
    }
	
    public static void main(String[] args) {
	SpringApplication.run(Main.class, args);
    }

    public Prototype getPrototype() {
	return account;
    }

    public PrototypeRepository getprototypeRepo() {
	return prototypeRepo;
    }
}
