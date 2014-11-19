package de.tudresden.swt14ws18.useraccountmanager;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

@Entity
public class Community extends Customer{

	@Id
	@GeneratedValue
	private long id;
	
	private Set<ConcreteCustomer> members = new HashSet<>();
	private ConcreteCustomer admin;
	@OneToOne
	private UserAccount userAccount;
	
	private CommunityRepository repository;
	
	public Community(String name, String password, UserAccount userAccount, ConcreteCustomer admin) {
		super(name, password);
		this.userAccount = userAccount;
		this.admin = admin;
		addMember(admin);
		repository.save(this);
	}
	
	public ConcreteCustomer getAdmin(){
		return admin;
	}
	
	public boolean addMember(ConcreteCustomer newMember){
		if(!members.contains(newMember)){
			members.add(newMember);
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isMember(ConcreteCustomer concreteCustomer){
		if(members.contains(concreteCustomer)){
			return true;
		}
		return false;
	}

}
