package de.tudresden.swt14ws18.useraccountmanager;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.GameType;

/**
 * Die Message-KLasse repräsentiert eine Mitteilung, die an einen Kunden geschickt wird, sollte er bei einem
 * Spiel seinen Einsatz nicht bezahlen können. Er wird von dem Spiel ausgeshlossen und bekommt eine Mitteilung, die
 * ihn 2€ Strafe kostet. Bei 10 Mitteilungen wird der Kunde gesperrt.
 * 
 * 
 * @author Reinhard_2
 *
 */

@Entity
public class Message {
    
    @Id
    @GeneratedValue
    private long id;
    
    
    private LocalDateTime date;
    private GameType whichGame;
    
    @Deprecated
    public Message(){
        
    }
    
    /**
     * Der Konstruktor
     * 
     * @param type      -> gibt an, von welchem Typ das Spiel war, bei dem der Kunde nicht bezahlen konnte
     */
    public Message(GameType type){
        this.whichGame = type;
        this.date = Lotterie.getInstance().getTime().getTime();
        Lotterie.getInstance().getMessagesRepository().save(this);
    }
    
    /**
     * 
     * 
     * @return          -> das Datum, an dem die Mitteilung erstellt wurde = das Datum, an dem nicht bezahlt werden 
     *                     konnte
     */
    
    public LocalDateTime getMessageDate(){
        return date;
    }
    
    /**
     * 
     * @return          -> gibt den Typ des Spiel zurück, bei dem die Message ausgelöst wurde
     */
    
    public GameType getWhichGameTypeItWas(){
        return whichGame;
    }

    public long getId() {
	return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
