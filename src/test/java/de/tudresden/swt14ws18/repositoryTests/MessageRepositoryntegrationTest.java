package de.tudresden.swt14ws18.repositoryTests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.useraccountmanager.Message;

public class MessageRepositoryntegrationTest extends AbstractIntegrationTest {

    @Autowired MessageRepository MessageRepo;
    
    @Test
    public void findAllMessages() {
        
        long size = MessageRepo.count();
        int sizeInt = new BigDecimal(size).intValueExact();
        
        Iterable<Message> messages = MessageRepo.findAll();
        
        assertThat(messages, is(iterableWithSize(sizeInt)));
        
    }
    
    @Test
    public void addAMessage(){
        
        Message tempMsg = new Message(GameType.LOTTO);
        
        MessageRepo.save(tempMsg);
        
        assertThat(MessageRepo.findAll(), hasItem(tempMsg));
        
        assertThat(MessageRepo.findOne(tempMsg.getId()), is(tempMsg));
    }

}
