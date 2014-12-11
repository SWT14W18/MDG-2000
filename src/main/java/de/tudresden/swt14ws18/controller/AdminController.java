package de.tudresden.swt14ws18.controller;

import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.tudresden.swt14ws18.Lotterie;
import de.tudresden.swt14ws18.bank.Transaction;
import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
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
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.tips.TotoTip;
import de.tudresden.swt14ws18.util.Constants;

@Controller
@PreAuthorize("hasRole('ROLE_BOSS')")
public class AdminController extends ControllerBase {

    @Autowired
    public AdminController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
    }

    private Comparator<LottoGame> comp = new Comparator<LottoGame>() {
        @Override
        public int compare(LottoGame lottoGame1, LottoGame lottoGame2) {
            return lottoGame1.getDate().compareTo(lottoGame2.getDate());
        }
    };

    @RequestMapping("/betsOverview")
    public String betsOverview(ModelMap map) {
        handleGeneralValues(map);

        Entry<LottoGame, Double> lottoGameInput = createLottoOverview().firstEntry();
        map.addAttribute("nextLottoDate", lottoGameInput.getKey().getDateString());
        map.addAttribute("nextLottoInput", Constants.MONEY_FORMAT.format(lottoGameInput.getValue()));

        map.addAttribute("liga1nextMatchDay", getMatchDayInput(TotoGameType.BUNDESLIGA1));
        map.addAttribute("liga2nextMatchDay", getMatchDayInput(TotoGameType.BUNDESLIGA2));
        map.addAttribute("liga1TotalInput", getTotalInput(TotoGameType.BUNDESLIGA1));
        map.addAttribute("liga2TotalInput", getTotalInput(TotoGameType.BUNDESLIGA2));
        map.addAttribute("pokalTotalInput", getTotalInput(TotoGameType.POKAL));

        return "statistics/betsOverview";
    }

    @RequestMapping("/lottoOverview")
    public String lottoOverview(ModelMap map) {
        handleGeneralValues(map);
        map.addAttribute("lottoGameInput", createLottoOverview());
        return "statistics/lottoOverview";
    }

    @RequestMapping("/totoOverview")
    public String totoOverview(@RequestParam("gameType") String gameTypeString, ModelMap map) {
        handleGeneralValues(map);
        TotoGameType totoGameType = TotoGameType.valueOf(gameTypeString);
        map.addAttribute("matchDayInput", getMatchDayInput(totoGameType));
        map.addAttribute("totoGameType", totoGameType.name());
        return "statistics/totoOverview";
    }

    @RequestMapping("/totoMatchOverview")
    public String totoMatchOverview(@RequestParam("liga") String liga, @RequestParam("matchDay") int matchDay, ModelMap map) {
        handleGeneralValues(map);
        TotoGameType totoGameType = TotoGameType.valueOf(liga);
        map.addAttribute("totoMatches", totoMatchRepository.findByMatchDayAndTotoGameType(matchDay, totoGameType));
        return "statistics/totoMatchOverview";
    }

    @RequestMapping("/vorspulen")
    public String vorspulen(@RequestParam("time") long minutes, ModelMap map) {
        time.forward(Duration.ofMinutes(minutes));
        return "redirect:time";
    }

    @RequestMapping("/time")
    public String vorspulen(ModelMap map) {
        handleGeneralValues(map);
        return "admin/time";
    }

    @RequestMapping("inOutOverview")
    public String inoutoverview(ModelMap map) {
        handleGeneralValues(map);

        double zufluesse = 0;
        double abfluesse = 0;

        for (Transaction trans : transactionRepo.findByFrom(Lotterie.getInstance().getBankAccount())) {
            abfluesse = abfluesse + trans.getAmount();
        }

        for (Transaction trans : transactionRepo.findByTo(Lotterie.getInstance().getBankAccount())) {
            zufluesse = zufluesse + trans.getAmount();
        }

        map.addAttribute("zufluesse", zufluesse);
        map.addAttribute("ausgaben", abfluesse);

        return "statistics/inOutOverview";
    }

    @RequestMapping("/statisticsoverview")
    public String statisticsoverview(ModelMap map) {
        handleGeneralValues(map);

        map.addAttribute("customers", customerRepository.count());

        return "statistics/overview";
    }

    @RequestMapping("/setTotoResult")
    public String insertTotoResult(@RequestParam("id") long id, @RequestParam("result") TotoResult result, ModelMap map) {
        TotoMatch match = totoMatchRepository.findById(id);
        
        if(match.getDate().isAfter(time.getTime()) || result == TotoResult.NOT_PLAYED)
            return "index";
            
        match.setResult(result);
        totoMatchRepository.save(match);
            
        return "index";
    }
    
    @RequestMapping("/setLottoNumbers")
    public String insertLottoNumbers(@RequestParam Map<String, String> params, ModelMap map) {

        LottoNumbers numbers = parseInput(params);

        if (numbers == null)
            return "index";

        LottoGame result = null;
        for (LottoGame match : lottoMatchRepository.findByResultOrderByDateAsc(null)) {
            if (!match.getDate().isBefore(time.getTime()))
                continue;

            result = match;
            break;
        }

        if (result != null) {
            result.setResult(numbers);
            lottoMatchRepository.save(result);
        }

        return "index";
    }

    private TreeMap<LottoGame, Double> createLottoOverview() {
        TreeMap<LottoGame, Double> lottoTipsMap = new TreeMap<LottoGame, Double>(comp);
        // TODO mit richtiger Zeit arbeiten lassen
        for (LottoGame lottoGame : lottoMatchRepository.findAll()/* AfterOrderByDateAsc() */) {
            if (lottoGame.isFinished() || lottoGame.getDate().isBefore(time.getTime()))
                continue;

            if (!lottoTipsMap.containsKey(lottoGame))
                lottoTipsMap.put(lottoGame, 0.0D);

            for (LottoTip lottoTip : lottoTipRepository.findByLottoGame(lottoGame))
                lottoTipsMap.put(lottoGame, lottoTipsMap.get(lottoGame) + lottoTip.getInput());
        }

        return lottoTipsMap;
    }

    private double getTotalInput(TotoGameType totoGameType) {
        double value = 0;

        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
            value += getMatchInput(match);
        }
        return value;
    }

    private TreeMap<Integer, Double> getMatchDayInput(TotoGameType totoGameType) {
        TreeMap<Integer, Double> matchDayInput = new TreeMap<>();
        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {
            if (time.getTime().isBefore(match.getDate())) {
                if (!matchDayInput.containsKey(match.getMatchDay())) {
                    matchDayInput.put(match.getMatchDay(), getMatchInput(match));
                } else {
                    matchDayInput.put(match.getMatchDay(), matchDayInput.get(match.getMatchDay()) + getMatchInput(match));
                }
            }
        }
        return matchDayInput;
    }

    private double getMatchInput(TotoMatch totoMatch) {
        double value = 0;
        for (TotoTip tip : totoTipRepository.findByTotoMatch(totoMatch))
            if (tip.isValid())
                value += tip.getInput();
        return value;

    }

}
