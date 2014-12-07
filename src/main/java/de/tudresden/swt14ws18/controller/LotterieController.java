package de.tudresden.swt14ws18.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.gamemanagement.GameType;
import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
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
import de.tudresden.swt14ws18.tips.Tip;
import de.tudresden.swt14ws18.tips.TipCollection;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.useraccountmanager.Community;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;
import de.tudresden.swt14ws18.useraccountmanager.Message;
import de.tudresden.swt14ws18.useraccountmanager.Status;

@Controller
public class LotterieController extends ControllerBase {
    @Autowired
    public LotterieController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    @RequestMapping({ "/", "/index" })
    public String Toindex(ModelMap map) {
        handleGeneralValues(map);
        return "index";
    }

    @RequestMapping("/login")
    public String login(ModelMap map) {

        handleGeneralValues(map);
        return "index";
    }
    
    /* TODO bitte hier die Überprüfung obs korrekt ist oder nicht (Tutor fragen ? ) 
    @RequestMapping(value = "/trylogin", method = RequestMethod.POST)
    public String trylogin(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap map) {
        
        return "index";
    }
    */
    @RequestMapping("/impressum")
    public String getImpressum(ModelMap map) {
        handleGeneralValues(map);
        return "impressum";
    }

    @RequestMapping("/logout")
    public String logout(ModelMap map) {

        handleGeneralValues(map);
        return "logout";
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public String reg(@RequestParam("username") String vorname, @RequestParam("password") String passwort, ModelMap map) {

        handleGeneralValues(map);
        
        if(vorname.isEmpty()){
            map.addAttribute("registrationError", true);
            return "forward:registration";
        }
        if(passwort.isEmpty()){
                map.addAttribute("registrationError", true);
                return "forward:registration";
        }
        
        if (userAccountManager.contains(new UserAccountIdentifier(vorname))) {
            map.addAttribute("registrationError", true);
            return "forward:registration";
        }

        final Role customerRole = new Role("ROLE_USER");

        UserAccount ua = userAccountManager.create(vorname, passwort, customerRole);
        userAccountManager.save(ua);

        BankAccount ba = new BankAccount();

        ConcreteCustomer c1 = new ConcreteCustomer(vorname, passwort, Status.ACTIVE, ua, ba);

        bankAccountRepository.save(ba);
        customerRepository.save(c1);
        map.addAttribute("registrationSuccess", true);
        return "forward:index";
    }

    @RequestMapping("/registration")
    public String registration(ModelMap map) {

        handleGeneralValues(map);
        return "registration";
    }

    // TODO alles unter diesem Kommentar sollte in einen eigenen Controller verschoben werden, je nachdem welcher Rolle die Funktion zugewiesen ist

    @RequestMapping("/payMessage")
    public String payMessage(@RequestParam("id") long id, ModelMap map) {
        Message message = messageRepo.findById(id);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

        if (customer.hasMessage(message))
            customer.payOneMessage(message);

        return "forward:profil";
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

        tips.addAll(lottoTipCollectionRepo.findByOwner(customer));
        tips.addAll(totoTipCollectionRepo.findByOwner(customer));

        return tips;
    }

    @RequestMapping("/toto")
    public String toto(ModelMap map) {
        handleGeneralValues(map);

        map.addAttribute("totoGameTypes", TotoGameType.values());
        //
        // Map<TotoGameType, Set<Integer>> dates = getRemainingMatchDates();
        //
        // map.addAttribute("games", dates.entrySet());
        return "games/toto";
    }

    private Map<TotoGameType, Set<Integer>> getRemainingMatchDates() {
        Map<TotoGameType, Set<Integer>> list = new HashMap<>();

        for (TotoMatch match : totoRepo.findByTotoResult(TotoResult.NOT_PLAYED)) {

            LocalDateTime localTime = time.getTime();
            LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);
            if (!localTime.isAfter(date)) {
                if (!list.containsKey(match.getTotoGameType()))
                    list.put(match.getTotoGameType(), new TreeSet<Integer>());

                list.get(match.getTotoGameType()).add(match.getMatchDay());
            }

        }

        return list;
    }

    @RequestMapping("/tipCollectionView")
    public String tipCollectionView(ModelMap map, @RequestParam("id") long tippscheinId, @RequestParam("game") GameType spielType) {
        handleGeneralValues(map);

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

    @RequestMapping("/totoMatchDays")
    public String totoMatchDays(@RequestParam("id") String totoGameTypeString, ModelMap map) {

        handleGeneralValues(map);
        TotoGameType totoGameType = TotoGameType.valueOf(totoGameTypeString);

        Set<Integer> set = new TreeSet<>();
        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
            LocalDateTime localTime = time.getTime();
            LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);
            if (!localTime.isAfter(date)) {
                set.add(match.getMatchDay());
            }
        }
        map.addAttribute("matchDays", set);
        map.addAttribute("liga", totoGameType.name());
        return "games/totoMatchDays";
    }

    @RequestMapping("/totoTipp")
    public String totoTipp(@RequestParam("liga") String liga, @RequestParam("id") int id, ModelMap map) {
        handleGeneralValues(map);

        TotoGameType totoGameType = TotoGameType.valueOf(liga);
        List<TotoMatch> list = new ArrayList<>();

        for (TotoMatch match : totoRepo.findByMatchDayAndTotoGameType(id, totoGameType)) {
            if (match.isFinished())
                continue;

            LocalDateTime localTime = time.getTime();
            LocalDateTime date = match.getDate().minusMinutes(MINUTES_BEFORE_DATE);

            if (!localTime.isAfter(date)) {
                list.add(match);
            }
        }

        map.addAttribute("matches", list);
        return "games/totoTipp";
    }

    @RequestMapping("/lotto")
    public String lotto(ModelMap map) {

        handleGeneralValues(map);
        return "games/lotto";
    }

    @RequestMapping(value = "/createLottoTip", method = RequestMethod.POST)
    public String createLottoTip(@RequestParam Map<String, String> params, ModelMap map) {

        handleGeneralValues(map);
        if(params.isEmpty()){
            map.addAttribute("lottoError", true);
            return "forward:/lotto";
        }
        
        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

            if(!tipFactory.craftLottoTips(params, customer)){
                map.addAttribute("lottoError", true);
                return "forward:/lotto";
            }
            map.addAttribute("lottoSuccess", true);
        }
        return "forward:gameoverview";
    }

    @RequestMapping(value = "/createTotoTip", method = RequestMethod.POST)
    public String createTotoTip(@RequestParam Map<String, String> params, ModelMap map) {

        handleGeneralValues(map);

        if (authenticationManager.getCurrentUser().isPresent()) {

            ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());

            tipFactory.craftTotoTips(params, customer);
            map.addAttribute("totoSuccess", true);
        }
        return "forward:gameoverview";
    }

    @RequestMapping("/groupoverview")
    public String groupoverview(ModelMap map) {

        handleGeneralValues(map);
        ConcreteCustomer customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get());
        List<Community> community = communityRepository.findByMembers(customer);
        map.addAttribute("groupoverview", community);
        return "groups/overview";
    }

    @RequestMapping("/groupcreate")
    public String groupcreate(ModelMap map) {

        handleGeneralValues(map);
        // Community community = CommunityRepository.(name, password,
        // userAccount, admin);
        // map.addAttribute("cName", community.getCommunityName());
        // map.addAttribute("cPassword", community.getCommunityPassword());
        return "groups/create";
    }

    @RequestMapping("/groupjoin")
    public String groupjoin(ModelMap map) {

        handleGeneralValues(map);
        // Community community = CommunityRepository.();
        // map.addAttribute("cName", community.getCommunityName());
        // map.addAttribute("cPassword", community.getCommunityPassword());
        return "groups/join";
    }

    @RequestMapping("/groupmanage")
    public String groupmanage(ModelMap map) {

        handleGeneralValues(map);
        // Community community = CommunityRepository.();
        // map.addAttribute("", community.getCommunityName());
        return "groups/manage";
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

    @RequestMapping("/bankaccount")
    public String bankaccount(ModelMap map) {

        handleGeneralValues(map);
        return "bankaccount";
    }

}
