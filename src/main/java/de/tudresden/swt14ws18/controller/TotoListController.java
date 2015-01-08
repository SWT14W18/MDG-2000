package de.tudresden.swt14ws18.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.util.Constants;

@Controller
@PreAuthorize("hasRole('ROLE_TOTOLIST')")
public class TotoListController extends ControllerBase{

    @Autowired
    public TotoListController(UserAccountManager userAccountManager, CustomerRepository customerRepository,
            CommunityRepository communityRepository, AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository,
            TotoMatchRepository totoRepo, LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo,
            BusinessTime time, TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo, lottoTipCollectionRepo,
                totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository, totoMatchRepository, messageRepo, tipFactory,
                transactionRepo);
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
        map.addAttribute("totoGameType", totoGameType.toString());
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for(Role r : it) {
            if(r.toString().equals("ROLE_BOSS"))
            {
                return "admin/lotterydrawtotomatchday";
            }
        }
        return "games/totoMatchDays";
    }
    @RequestMapping("/ADMINtotoMatchDays")
    public String admintotoMatchDays(@RequestParam("id") String totoGameTypeString, ModelMap map) {

        handleGeneralValues(map);
        TotoGameType totoGameType = TotoGameType.valueOf(totoGameTypeString);

        Set<Integer> set = new TreeSet<>();
        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
            if(!match.isFinished() && match.getDate().isBefore(time.getTime())){
            set.add(match.getMatchDay());
            }
        }
        map.addAttribute("matchDays", set);
        map.addAttribute("liga", totoGameType.name());
        map.addAttribute("totoGameType", totoGameType.toString());
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for(Role r : it) {
            if(r.toString().equals("ROLE_BOSS"))
            {
                return "admin/lotterydrawtotomatchday";
            }
        }
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
        map.addAttribute("totoGameType", totoGameType.toString());
        map.addAttribute("matchDay", id);
        map.addAttribute("groups", communityRepository.findByMembers(getCurrentUser()));
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for(Role r : it) {
            if(r.toString().equals("ROLE_BOSS"))
            {   
                return "admin/lotterydrawsettoto";
            }
        }
        return "games/totoTipp";
    }
    
    @RequestMapping("/ADMINtotoTipp")
    public String admintotoTipp(@RequestParam("liga") String liga, @RequestParam("id") int id, ModelMap map) {
        handleGeneralValues(map);

        TotoGameType totoGameType = TotoGameType.valueOf(liga);
        List<TotoMatch> list = new ArrayList<>();

        for (TotoMatch match : totoRepo.findByMatchDayAndTotoGameType(id, totoGameType)) {

            if(!match.isFinished() && match.getDate().isBefore(time.getTime())){
                list.add(match);
            }
        }

        map.addAttribute("matches", list);
        map.addAttribute("totoGameType", totoGameType.toString());
        map.addAttribute("matchDay", id);
        map.addAttribute("groups", communityRepository.findByMembers(getCurrentUser()));
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for(Role r : it) {
            if(r.toString().equals("ROLE_BOSS"))
            {
                return "admin/lotterydrawsettoto";
            }
        }
        return "games/totoTipp";
    }
    
    @RequestMapping("/toto")
    public String toto(ModelMap map) {
        handleGeneralValues(map);

        map.addAttribute("groups", communityRepository.findByMembers(getCurrentUser()));

        map.addAttribute("totoGameTypes", TotoGameType.values());
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for(Role r : it) {
            if(r.toString().equals("ROLE_BOSS") )
            {
                return "admin/lotterydrawtoto";
            }
        }
        return "games/toto";
    }
}
