package de.tudresden.swt14ws18.useraccountmanager;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.GameType;

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
    
    public Message(GameType type){
        this.whichGame = type;
        this.date = Lotterie.getInstance().getTime().getTime();
    }
    
    public LocalDateTime getMessageDate(){
        return date;
    }
    
    public GameType getWhichGameTypeItWas(){
        return whichGame;
    }

}
