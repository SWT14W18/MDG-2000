package de.tudresden.swt14ws18.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import de.tudresden.swt14ws18.util.Constants;

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
        return "games/toto";
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
            LocalDateTime date = match.getDate().minusMinutes(Constants.MINUTES_BEFORE_DATE);
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
            LocalDateTime date = match.getDate().minusMinutes(Constants.MINUTES_BEFORE_DATE);

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

}