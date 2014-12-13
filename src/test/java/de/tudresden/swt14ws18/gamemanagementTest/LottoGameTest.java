package de.tudresden.swt14ws18.gamemanagementTest;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.AbstractIntegrationTest;
import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;
import de.tudresden.swt14ws18.gamemanagement.Game;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.LottoResult;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Message;
import de.tudresden.swt14ws18.useraccountmanager.Status;
import de.tudresden.swt14ws18.util.Constants;

/**
 * Dieser Test baut auf die funktionierenden RepositoryTests auf!
 * 
 * @author Reinhard_2
 *
 */

public class LottoGameTest extends AbstractIntegrationTest {

    @Autowired LottoMatchRepository matchRepo;
 
    
    @Test 
    public void testConstructorLottoGame(){
        LocalDateTime time = Lotterie.getInstance().getTime().getTime();
        LottoGame g = new LottoGame(time);
        matchRepo.save(g);
        assertEquals(g,matchRepo.findByDate(time));
    }

    
    @Test
    public void testWinningPot(){
        LocalDateTime time = Lotterie.getInstance().getTime().getTime();
        Map<LottoResult, Double> winLevels = new HashMap<>();
        LottoGame g = new LottoGame(time);
        double winpot = 1;
        
        winLevels.put(LottoResult.NONE, 0 * winpot);
        winLevels.put(LottoResult.TWO_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.THREE, 0.1 * winpot);
        winLevels.put(LottoResult.THREE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR, 0.1 * winpot);
        winLevels.put(LottoResult.FOUR_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE, 0.1 * winpot);
        winLevels.put(LottoResult.FIVE_SUPER, 0.1 * winpot);
        winLevels.put(LottoResult.SIX, 0.1 * winpot);
        winLevels.put(LottoResult.SIX_SUPER, 0.2 * winpot);
        
        g.setWinningPot(winpot);
        matchRepo.save(g);
        assertEquals(g.getWinningPot(),winLevels);
    }
    


}