package de.tudresden.swt14ws18.useraccountmanager;

import javax.persistence.MappedSuperclass;

/**
 * Die Klasse Customer ist die Superklasse von Community und ConcreteCustomer.
 * Anfangs als abstrakte Klasse gedacht, die in einem Repository verwaltet wird, ist sie nur noch
 * als Superklasse vorhanden und gibt Sowohl der Sommunity, als auch dem ConcreteCustomer einen Namen
 * und ein Passwort.
 * Die Anweisung @MappedSuperclass dient Spring als Erkennung, dass diese KLasse lediglich eine Superklasse
 * ist, die nicht weiter beachtet werden oder gar als Entity eingestuft werden muss.
 * @author Reinhard_2
 *
 */

@MappedSuperclass
public class Customer {
	
	private String name;
	private String password;
	
	@Deprecated
	protected Customer() {
	}
	
	public Customer(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPassword(){
		return password;
	}
}
