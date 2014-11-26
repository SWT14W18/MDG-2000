package de.tudresden.swt14ws18.useraccountmanager;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;


/**
 * die Klasse "Community" leitet sich wie auch der ConcreteCustomer vom Customer ab und besitzt
 * somit einen Namen und ein Passwort.
 * 
 * Die Community bildet die Gewinngemeinschaft nach, als welche sich Kudnen zusammenschließen können,
 * um gemeinsam am Glücksspiel teilzunehmen.
 * Der Ersteller der Gruppe ist gleichzeitig der Administrator der Gruppe, der Tippscheine kaufen kann.
 * Die restlichen "normalen" Mitglieder können sich an dem Tipp beteiligen, jedoch prozentual nur so weit
 * bis die Grenze erreicht ist, die der Admin gerne selbst beteiligt sein möchte. (z.B. Admin kauft Tippschein
 * mit 20% festgelegter Beteiligung für sich. Alle anderen können die verbleibenden 80% unter sich aufteilen)
 * 
 * Communities sind genau wie ConcreteCustomer an genau einen UserAccount gebunden, besitzen jedoch keinen
 * BankAccount, da das Geld direkt von den Konten der ConcreteCustomer kommt.
 * 
 * @param members: Ein HashSet der Mitglieder der Gemeinschaft.
 * 
 * @param admin: Der Administrator der Gruppe
 * 
 * @param userAccount: Der Useraccount, der der Gemeinschaft zugeordnet ist
 * 
 *
 *
 * @author Reinhard_2
 *
 */
@Entity
public class Community extends Customer{

	@Id
	@GeneratedValue
	private long id;
	
	@OneToMany
	private Set<ConcreteCustomer> members = new HashSet<>();
	
	@OneToOne
	private ConcreteCustomer admin;
	
	@OneToOne
	private UserAccount userAccount;
	
	/**
	 * Konstruktor!
	 * 
	 * @param name			-> Name der Gemeinschaft (frei wählbar)
	 * @param password		-> Das Passwort, mit dem man in die Gruppe gelangt (vom Admin(ersteller) festgelegt)
	 * @param userAccount	-> der eindeutig zugeordnete UserAccount (wird beim erstellen neu angelegt)
	 * @param admin			-> der Kunde, der der Admin der Gruppe sein soll (im Normalfall der Ersteller)
	 */
	
	public Community(String name, String password, UserAccount userAccount, ConcreteCustomer admin) {
		super(name, password);
		this.userAccount = userAccount;
		this.admin = admin;
		addMember(admin);
	}
	
	public ConcreteCustomer getAdmin(){
		return admin;
	}
	
	/**
	 * Wenn ein Kunde hinzugefügt werden soll, muss geprüft werden, ob er schon Mitglied der Gruppe ist.
	 * 
	 * @param newMember		-> Das neue Mitglied vom Typ "ConcreteCustomer"
	 * @return
	 */
	
	public boolean addMember(ConcreteCustomer newMember){
		if(!isMember(newMember)){
			members.add(newMember);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Ist ein Kunde bereits Mitglied?
	 * 
	 * @param concreteCustomer		-> für diesen Kudnen prüfen
	 * @return
	 */
	
	public boolean isMember(ConcreteCustomer concreteCustomer){
		if(members.contains(concreteCustomer)){
			return true;
		}
		return false;
	}

  }
