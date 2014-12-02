package de.tudresden.swt14ws18.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.LottoTipRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.tips.TotoTip;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Controller
public class AdminController {

    private final UserAccountManager userAccountManager;
    private final CustomerRepository customerRepository;
    private final CommunityRepository communityRepository;
    private final AuthenticationManager authenticationManager;
    private final BankAccountRepository bankAccountRepository;
    private final TotoMatchRepository totoRepo;
    private final LottoTipCollectionRepository lottoTipCollectionRepo;
    private final TotoTipCollectionRepository totoTipCollectionRepo;
    private final TotoTipRepository totoTipRepository;
    private final LottoTipRepository lottoTipRepository;
    private final TotoMatchRepository totoMatchRepository;
    private final BusinessTime time;
    private final LottoMatchRepository lottoMatchRepository;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static final long MINUTES_BEFORE_DATE = 5;
    private Map<TotoGameType, Double> totoGameTypeInput = new HashMap<>();
    private Map<Integer, Double> liga1MatchDayInput = new HashMap<>();
    private Map<Integer, Double> liga2MatchDayInput = new HashMap<>();
    private Map<Integer, Double> pokalMatchDayInput = new HashMap<>();
    

    @Autowired
    public AdminController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
	    AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository,
	    TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo,
	    BusinessTime time, TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
	    TotoMatchRepository totoMatchRepository) {
	Assert.notNull(authenticationManager, "UserAccountManager must not be null!");
	Assert.notNull(bankAccountRepository, "UserAccountManager must not be null!");
	Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
	Assert.notNull(customerRepository, "CustomerRepository must not be null!");
	Assert.notNull(communityRepository, "CommunityRepository must not be null");

	this.time = time;
	this.totoRepo = totoRepo;
	this.userAccountManager = userAccountManager;
	this.customerRepository = customerRepository;
	this.communityRepository = communityRepository;
	this.bankAccountRepository = bankAccountRepository;
	this.authenticationManager = authenticationManager;
	this.lottoTipCollectionRepo = lottoTipCollectionRepo;
	this.totoTipCollectionRepo = totoTipCollectionRepo;
	this.totoTipRepository = totoTipRepository;
	this.lottoMatchRepository = lottoMatchRepository;
	this.lottoTipRepository = lottoTipRepository;
	this.totoMatchRepository = totoMatchRepository;
    }

    @RequestMapping("/betsOverview")
    public String betsOverview(ModelMap map) {
    	handleGeneralValues(map);

    	map.addAttribute("lottoTipsMap", lottoOverview());	
    	map.addAttribute("tips", totoTipRepository.findAll());
    	
    	totoOverview();
    	map.addAttribute("liga1MatchDayInput", liga1MatchDayInput);
    	map.addAttribute("liga2MatchDayInput", liga2MatchDayInput);
    	map.addAttribute("totoGameTypeInput", totoGameTypeInput);
    	

    	return "statistics/betsOverview";
    }
    
    
    private Map<LottoGame, Double> lottoOverview(){
    	Comparator<LottoGame> comp = new Comparator<LottoGame>(){
    		@Override
    		public int compare(LottoGame lottoGame1, LottoGame lottoGame2){
    			return lottoGame1.getDate().compareTo(lottoGame2.getDate());
    		}
    	};
    	Map<LottoGame, Double> lottoTipsMap = new TreeMap<LottoGame, Double>(comp);
    	for(LottoGame lottoGame : lottoMatchRepository.findByDateAfterOrderByDateAsc(LocalDateTime.of(2014, 12, 2, 19, 0))){
    		if(lottoTipRepository.findByLottoGame(lottoGame).isEmpty())lottoTipsMap.put(lottoGame, 0.0D);
    		for(LottoTip lottoTip : lottoTipRepository.findByLottoGame(lottoGame)){
    			if(!lottoTipsMap.containsKey(lottoGame)){lottoTipsMap.put(lottoGame, lottoTip.getInput());}
    		lottoTipsMap.put(lottoGame, lottoTipsMap.get(lottoGame)+lottoTip.getInput());
    		}    		
    	} 
    	return lottoTipsMap;   	
    }
    
    
    private void totoOverview(){
    	liga1MatchDayInput = totoMatchDayOverview(TotoGameType.BUNDESLIGA1);
    	liga2MatchDayInput = totoMatchDayOverview(TotoGameType.BUNDESLIGA2);
//    	pokalMatchDayInput = totoMatchDayOverview(TotoGameType.POKAL);
    	
    }
    
    private Map<Integer, Double> totoMatchDayOverview(TotoGameType totoGameType){
    	Map<Integer, Double> matchDayInput = new HashMap<Integer, Double>();
    	for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
    	    LocalDateTime localTime = time.getTime();
    	    LocalDateTime date = match.getDate();
    	    if (!localTime.isAfter(date)) {
        		if(!totoGameTypeInput.containsKey(match.getTotoGameType())){totoGameTypeInput.put(match.getTotoGameType(), match.getTotalInput());}
				totoGameTypeInput.put(totoGameType, totoGameTypeInput.get(totoGameType)+match.getTotalInput());
    	    	
    	    	if(!matchDayInput.containsKey(match.getMatchDay())){matchDayInput.put(match.getMatchDay(), match.getTotalInput());}
    	    	else{matchDayInput.put(match.getMatchDay(), matchDayInput.get(match.getMatchDay())+match.getTotalInput());}
    	    }
    	}
    	return matchDayInput;  
    }
    	
    	
    	
    	
    	
    	
    	
//    	Map<TotoMatch, Map<TotoResult, Double>> totoMatchInput = new HashMap<>();
//    	for(TotoMatch totoMatch : totoMatchRepository.findAll()){
//    		Map<TotoResult, Double> totoMatchResultInput = new HashMap<>();
//    		for(TotoTip totoTip : totoTipRepository.findAll()){
//    			if(!totoMatchResultInput.containsKey(totoTip.getResult()))totoMatchResultInput.put(totoTip.getResult(), totoTip.getInput());
//    			totoMatchResultInput.put(totoTip.getResult(), totoMatchResultInput.get(totoTip.getResult())+totoTip.getInput());
//    		}
//    		totoMatchInput.put(totoMatch, totoMatchResultInput);
//    	}
    
    

    public void handleGeneralValues(ModelMap map) {
	if (authenticationManager.getCurrentUser().isPresent()) {
	    ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

	    map.addAttribute("balance", new DecimalFormat("#0.00").format(customer.getAccount().getBalance()));

	}

    }

}
