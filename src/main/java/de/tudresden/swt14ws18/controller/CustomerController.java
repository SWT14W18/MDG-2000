package de.tudresden.swt14ws18.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.BankAccountRepository;
import de.tudresden.swt14ws18.repositories.CommunityRepository;
import de.tudresden.swt14ws18.repositories.CustomerRepository;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.LottoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.LottoTipRepository;
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.LottoTipCollection;
import de.tudresden.swt14ws18.tips.Tip;
import de.tudresden.swt14ws18.tips.TipCollection;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.tips.TipShare;
import de.tudresden.swt14ws18.tips.TotoTip;
import de.tudresden.swt14ws18.tips.TotoTipCollection;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Message;

@Controller
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
public class CustomerController extends ControllerBase {

    @Autowired
    public CustomerController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    @RequestMapping("/payMessage")
    public String payMessage(@RequestParam("id") long id, ModelMap map) {
        Message message = messageRepo.findById(id);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

        if (customer.hasMessage(message))
            customer.payOneMessage(message);

        return "forward:profil";
    }

    @RequestMapping("/groupoverview")
    public String groupoverview(ModelMap map) {

        handleGeneralValues(map);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
        List<Community> community = communityRepository.findByMembers(customer);
        map.addAttribute("groupoverview", community);
        return "groups/overview";
    }

    @RequestMapping(value = "/groupcreate", method = RequestMethod.POST)
    public String create(ModelMap map, @RequestParam("cName") String name) {

        handleGeneralValues(map);
        ConcreteCustomer admin = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
        String password = Community.createPassword();   // Random Passwort hinzufügen
        while(communityRepository.findByPassword(password) != null)
        	password = Community.createPassword();
        communityRepository.save(new Community(name, password, admin));
        return "groups/create";
    }

    @RequestMapping("/groupcreate")
    public String groupcreate(ModelMap map) {

        handleGeneralValues(map);
        return "groups/create";
    }

    @RequestMapping(value = "/groupjoin", method = RequestMethod.POST)
    public String join(ModelMap map, @RequestParam("cPassword") String password) {

        handleGeneralValues(map);
        Community community = communityRepository.findByPassword(password);
        
        if(community == null)
            return "groups/overview";
        
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
        community.addMember(customer);
        communityRepository.save(community);
        map.addAttribute("groupoverview", community);
        groupoverview(map);
        return "groups/overview";
    }

    @RequestMapping("/groupjoin")
    public String groupjoin(ModelMap map) {

        handleGeneralValues(map);
        return "groups/join";
    }
    
    @RequestMapping("/groupLeave")
    public String groupLeave(@RequestParam("id") long id, ModelMap map) {
        handleGeneralValues(map);
        
        Community com = communityRepository.findById(id);
        if(com.isMember(getCurrentUser()) && !com.getAdmin().equals(getCurrentUser()))
            com.removeMember(getCurrentUser());
        
        communityRepository.save(com);
        
        return groupoverview(map);
    }

    @RequestMapping("/groupmanage")
    public String groupmanage(ModelMap map, @RequestParam ("id") long id) {

        handleGeneralValues(map);

        // soll die gleiche Liste wie die Übersicht zeigen + Button zum ändern
        Community community = communityRepository.findById(id);
        Set<ConcreteCustomer> customer = community.getMemberList();
        map.addAttribute("groupmanage", customer);
        
        List<TipCollection<?>> tip = new ArrayList<>();
        tip.addAll(totoTipCollectionRepo.findByCommunity(community));
        tip.addAll(lottoTipCollectionRepo.findByCommunity(community));
        map.addAttribute("tips", tip);
        return "groups/manage";
    }
    
    @RequestMapping(value = "/tipCollection", method = RequestMethod.POST)
    public String tipCollectionPost(ModelMap map, @RequestParam("id") long tippscheinId, @RequestParam("game") GameType gameType, @RequestParam("remove") boolean remove) {
        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            List<Tip> set = new ArrayList<>();
            if (gameType == GameType.LOTTO) {
                set.addAll(lottoTipCollectionRepo.findOne(tippscheinId).getTips());
                LottoTipCollection col = lottoTipCollectionRepo.findById(tippscheinId);
                TipShare shares = col.getShares();
                
                double remain = 100 * shares.getRemainingShare();
                double share = 100 * shares.getShare(getCurrentUser());
                map.addAttribute("shares", share);
                map.addAttribute("remain", remain);
                
                if (remove) shares.removeShareholder(getCurrentUser());
            }
            if (gameType == GameType.TOTO) {
                set.addAll(totoTipCollectionRepo.findOne(tippscheinId).getTips());
                TotoTipCollection col = totoTipCollectionRepo.findById(tippscheinId);
                TipShare shares = col.getShares();
                
                double remain = 100 * shares.getRemainingShare();
                double share = 100 * shares.getShare(getCurrentUser());
                map.addAttribute("shares", share);
                map.addAttribute("remain", remain);
                
                if (remove) shares.removeShareholder(getCurrentUser());
            }
            map.addAttribute("tips", set);
            
            
        }
        map.addAttribute("id", tippscheinId);
        map.addAttribute("game", gameType.name());

        if (gameType == GameType.LOTTO)
            return "groups/lottoTipCollection";
        else if (gameType == GameType.TOTO)
            return "groups/totoTipCollection";
        else
            return "groups/tipCollection";
    }
    
    @RequestMapping("/tipCollection")
    public String tipCollection(ModelMap map, @RequestParam("id") long tippscheinId, @RequestParam("game") GameType gameType) {
        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            List<Tip> set = new ArrayList<>();
            if (gameType == GameType.LOTTO) {
                set.addAll(lottoTipCollectionRepo.findOne(tippscheinId).getTips());
                LottoTipCollection col = lottoTipCollectionRepo.findById(tippscheinId);
                TipShare shares = col.getShares();
                
                double remain = 100 * shares.getRemainingShare();
                double share = 100 * shares.getShare(getCurrentUser());
                map.addAttribute("shares", share);
                map.addAttribute("remain", remain);                
            }
            if (gameType == GameType.TOTO) {
                set.addAll(totoTipCollectionRepo.findOne(tippscheinId).getTips());
                TotoTipCollection col = totoTipCollectionRepo.findById(tippscheinId);
                TipShare shares = col.getShares();
                
                double remain = 100 * shares.getRemainingShare();
                double share = 100 * shares.getShare(getCurrentUser());
                map.addAttribute("shares", share);
                map.addAttribute("remain", remain);
            }
            map.addAttribute("tips", set);
            
        }
        map.addAttribute("id", tippscheinId);
        map.addAttribute("game", gameType.name());

        if (gameType == GameType.LOTTO)
            return "groups/lottoTipCollection";
        else if (gameType == GameType.TOTO)
            return "groups/totoTipCollection";
        else
            return "groups/tipCollection";
    }
    
    @RequestMapping(value = "/changeTotoTip", method = RequestMethod.POST)
    public String changeToto(ModelMap map, @RequestParam("percentage") double percentage, @RequestParam("id") long id, @RequestParam("game") GameType gametype){
        handleGeneralValues(map);
        
        TotoTipCollection col = totoTipCollectionRepo.findById(id);
        TipShare shares = col.getShares();
        percentage = percentage/100;
        shares.addShareholder(getCurrentUser(), percentage);
        double remain = 100 * shares.getRemainingShare();
        double share = 100 * shares.getShare(getCurrentUser());
        
        map.addAttribute("remain", remain);
        map.addAttribute("shares", share);
        map.addAttribute("id", id);
        map.addAttribute("game", gametype.name());
        
        totoTipCollectionRepo.save(col);
    	return "forward:tipCollection";
    }

    @RequestMapping(value = "/changeLottoTip", method = RequestMethod.POST)
    public String changeLotto(ModelMap map, @RequestParam("percentage") double percentage, @RequestParam("id") long id, @RequestParam("game") GameType gametype){
        handleGeneralValues(map);
        
        LottoTipCollection col = lottoTipCollectionRepo.findById(id);
        TipShare shares = col.getShares();
        percentage = percentage/100;
        shares.addShareholder(getCurrentUser(), percentage);
        double remain = 100 * shares.getRemainingShare();
        double share = 100 * shares.getShare(getCurrentUser());
        
        map.addAttribute("remain", remain);
        map.addAttribute("shares", share);
        map.addAttribute("id", id);
        map.addAttribute("game", gametype.name());
        
        lottoTipCollectionRepo.save(col);
    	return "forward:tipCollection";
    }
    
    @RequestMapping("/profil")
    @PreAuthorize("isAuthenticated()")
    public String profil(ModelMap map) {

        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
            map.addAttribute("name", customer.getName());
            map.addAttribute("messages", customer.getMessageCount());
            map.addAttribute("state", customer.getState());
            map.addAttribute("messageList", customer.getMessages());
        }
        return "profil";
    }

    @RequestMapping("/gameoverview")
    public String gameoverview(ModelMap map) {

        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

            map.addAttribute("tips", getTips(customer));
        }
        return "games/overview";
    }

    private List<TipCollection<?>> getTips(ConcreteCustomer customer) {
        List<TipCollection<?>> tips = new ArrayList<>();

        for(TipCollection<?> tip : lottoTipCollectionRepo.findByOwner(customer))
            if(!tip.isFinished())
                tips.add(tip);
        
        for(TipCollection<?> tip : totoTipCollectionRepo.findByOwner(customer))
            if(!tip.isFinished())
                tips.add(tip);
        
        //tips.addAll(lottoTipCollectionRepo.findByOwner(customer));
        //tips.addAll(totoTipCollectionRepo.findByOwner(customer));

        return tips;
    }


    @RequestMapping("/tipCollectionView")
    public String tipCollectionView(ModelMap map, @RequestParam("id") long tippscheinId, @RequestParam(value ="success", required = false) Boolean bool, @RequestParam("game") GameType spielType) {
        handleGeneralValues(map);

        if(bool != null)
            map.addAttribute(bool ? "tippChangeSuccess" : "tippChangeError", true);
        
        if (authenticationManager.getCurrentUser().isPresent()) {

            List<Tip> set = new ArrayList<>();
            if (spielType == GameType.LOTTO) {
                set.addAll(lottoTipCollectionRepo.findOne(tippscheinId).getTips());
            }
            if (spielType == GameType.TOTO) {
                set.addAll(totoTipCollectionRepo.findOne(tippscheinId).getTips());
            }
            map.addAttribute("tips", set);
        }

        if (spielType == GameType.LOTTO)
            return "games/lottoTipCollectionOverview";
        else if (spielType == GameType.TOTO)
            return "games/totoTipCollectionOverview";
        else
            return "games/tipCollectionView";
    }



    @RequestMapping("/lotto")
    public String lotto(ModelMap map) {
        handleGeneralValues(map);

        map.addAttribute("groups", communityRepository.findByMembers(getCurrentUser()));

        for (LottoGame lottoGame : lottoMatchRepository.findByResultOrderByDateAsc(null)) {
            if (lottoGame.getDate().isAfter(time.getTime())) {
                map.addAttribute("nextLottoGame", lottoGame.getDateString());
                map.addAttribute("nextLottoJackpot", lottoGame.getJackpot());
                break;
            }
        }

        return "games/lotto";
    }

    @RequestMapping("/deleteTotoTip")
    public String deleteTotoTip(@RequestParam("id") long id, ModelMap map) {
        handleGeneralValues(map);

        TotoTip tip = totoTipRepository.findOne(id);
        TotoTipCollection col = totoTipCollectionRepo.findByTips(tip);

        if (!col.getOwner().equals(getCurrentUser()) || tip.isFinished() || !tip.isValid() || col.isFinished())
            return "index";
        if (timeCheck(tip.getGame().getDate()))
            return "index";

        col.removeTip(tip);

        totoTipRepository.delete(tip);
        if (col.getTips().isEmpty())
            totoTipCollectionRepo.delete(col);

        return "index";
    }

    @RequestMapping("/deleteLottoTip")
    public String deleteLottoTip(@RequestParam("id") long id, ModelMap map) {
        handleGeneralValues(map);

        LottoTip tip = lottoTipRepository.findOne(id);
        LottoTipCollection col = lottoTipCollectionRepo.findByTips(tip);

        if (!col.getOwner().equals(getCurrentUser()) || tip.isFinished() || !tip.isValid() || col.isFinished())
            return "index";

        if (timeCheck(tip.getGame().getDate()))
            return "index";
        
        col.removeTip(tip);

        lottoTipRepository.delete(tip);
        if (col.getTips().isEmpty())
            lottoTipCollectionRepo.delete(col);

        return "index";
    }

    @RequestMapping("/totoTipChange")
    public String totoTipChange(@RequestParam("id") long id, ModelMap map) {
        handleGeneralValues(map);

        TotoTip tip = totoTipRepository.findOne(id);

        map.addAttribute("id", id);
        map.addAttribute("match", tip.getGame());
        
        return "games/totoTipChange";
    }
    
    @RequestMapping(value = "/editTotoTip", method = RequestMethod.POST)
    public String editTotoTip(@RequestParam("id") long id, @RequestParam("result") TotoResult result, @RequestParam("input") double input, ModelMap map) {

        TotoTip tip = totoTipRepository.findOne(id);
        TotoTipCollection col = totoTipCollectionRepo.findByTips(tip);
        
        if (!col.getOwner().equals(getCurrentUser()) || tip.isFinished() || !tip.isValid() || col.isFinished())
            return "redirect:gameoverview";

        if (timeCheck(tip.getGame().getDate()))
            return "redirect:gameoverview";
        
        tip.setResult(result);
        tip.setInput(input);
        totoTipRepository.save(tip);
        
        return "redirect:gameoverview";
    }

    @RequestMapping("/lottoTipChange")
    public String lottoTipChange(@RequestParam("id") long id, ModelMap map) {
        handleGeneralValues(map);
        LottoTip tip = lottoTipRepository.findOne(id);
        map.addAttribute("id", id);
        map.addAttribute("1", tip.getNumbers().getNumbers()[0]);
        map.addAttribute("2", tip.getNumbers().getNumbers()[1]);
        map.addAttribute("3", tip.getNumbers().getNumbers()[2]);
        map.addAttribute("4", tip.getNumbers().getNumbers()[3]);
        map.addAttribute("5", tip.getNumbers().getNumbers()[4]);
        map.addAttribute("6", tip.getNumbers().getNumbers()[5]);
        map.addAttribute("super", tip.getNumbers().getSuperNumber());        
        
        return "games/lottoTipChange";
    }
    
    @RequestMapping(value = "/editLottoTip", method = RequestMethod.POST)
    public String editLottoTip(@RequestParam("id") long id, @RequestParam Map<String, String> params, ModelMap map) {

        LottoTip tip = lottoTipRepository.findOne(id);
        LottoTipCollection col = lottoTipCollectionRepo.findByTips(tip);
        
        map.addAttribute("id", id);
        map.addAttribute("game", GameType.LOTTO.name());
                
        if (!col.getOwner().equals(getCurrentUser()) || tip.isFinished() || !tip.isValid() || col.isFinished())
        {
            map.addAttribute("success", false);
            return "redirect:tipCollectionView";
        }

        if (timeCheck(tip.getGame().getDate()))
        {
            map.addAttribute("success", false);
            return "redirect:tipCollectionView";
        }
        
        LottoNumbers numbers = parseInput(params);
        
        if(numbers == null)
        {
            map.addAttribute("success", false);
            return "redirect:tipCollectionView";
        }
        
        tip.setResult(numbers);
        lottoTipRepository.save(tip);

        map.addAttribute("success", true);
        return "redirect:tipCollectionView";
    }
}
