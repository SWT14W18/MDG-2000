package de.tudresden.swt14ws18.useraccountmanager;

/**
 * Eine kleine Enumeration, um den Status des Kunden zu repräsentieren. 
 * 
 * @ACTIVE	-> der Kunde ist normal aktiv, hat keine 10 Mitteilungen und kann sich normal anmelden
 * @CLOSED	-> Der Kunde ist gesperrt, weil er 10 MItteilungen besitzt
 * @ANONYM	-> Ein anonymes Konto repräsentierte eine reale Filiale, die stellvertretend für ihre Kunden
 * 			   Tippscheine erstellt
 * 
 * @author Reinhard_2
 *
 */
public enum Status {
	ACTIVE, CLOSED, ANONYM, BLOCKED

}
