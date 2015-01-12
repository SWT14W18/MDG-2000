package de.tudresden.swt14ws18.useraccountmanager;

/**
 * Eine kleine Enumeration, um den Status des Kunden zu repräsentieren. 
 * 
 * ACTIVE	- der Kunde ist normal aktiv, hat keine 10 Mitteilungen und kann sich normal anmelden
 * CLOSED	- Ein anonymes Konto, welches geschlossen wurde
 * ANONYM	- Ein anonymes Konto repräsentierte eine reale Filiale, die stellvertretend für ihre Kunden
 * 			   Tippscheine erstellt
 * BLOCKED      - Ein gesperrtes Konto, weil es 10 oder mehr Mitteilungen hat
 * 
 * @author Reinhard_2
 *
 */
public enum Status {
	ACTIVE, CLOSED, ANONYM, BLOCKED

}
