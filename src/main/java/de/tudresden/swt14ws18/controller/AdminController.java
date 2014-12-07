package de.tudresden.swt14ws18.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
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
import de.tudresden.swt14ws18.bank.BankAccount;
import de.tudresden.swt14ws18.bank.Transaction;
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
import de.tudresden.swt14ws18.repositories.MessageRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoTipCollectionRepository;
import de.tudresden.swt14ws18.repositories.TotoTipRepository;
import de.tudresden.swt14ws18.repositories.TransactionRepository;
import de.tudresden.swt14ws18.tips.LottoTip;
import de.tudresden.swt14ws18.tips.TipFactory;
import de.tudresden.swt14ws18.tips.TotoTip;

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

    // TODO wir sollten diese Daten nicht zwischenspeichern sondern immer frisch aus den Repos holen
    private Map<TotoGameType, Double> totoGameTypeInput = new HashMap<>();
    private TreeMap<Integer, Double> liga1MatchDayInput = new TreeMap<>();
    private TreeMap<Integer, Double> liga2MatchDayInput = new TreeMap<>();
    private TreeMap<Integer, Double> pokalMatchDayInput = new TreeMap<>();

    @RequestMapping("/betsOverview")
    public String betsOverview(ModelMap map) {
        handleGeneralValues(map);

        Entry<LottoGame, Double> lottoGameInput = createLottoOverview().firstEntry();
        map.addAttribute("nextLottoDate", lottoGameInput.getKey().getDateString());
        map.addAttribute("nextLottoInput", MONEY_FORMAT.format(lottoGameInput.getValue()));

        createTotoOverview();
        map.addAttribute("liga1MatchDayInput", liga1MatchDayInput);
        map.addAttribute("liga2MatchDayInput", liga2MatchDayInput);
        map.addAttribute("totoGameTypeInput", totoGameTypeInput);
        map.addAttribute("pokalMatchDayInput", pokalMatchDayInput);

        return "statistics/betsOverview";
    }

    @RequestMapping("/lottoOverview")
    public String lottoOverview(ModelMap map) {
        handleGeneralValues(map);
        map.addAttribute("lottoGameInput", createLottoOverview());
        return "statistics/lottoOverview";
    }

    @RequestMapping("/totoOverview")
    public String totoOverview(ModelMap map) {
        handleGeneralValues(map);
        map.addAttribute("liga1MatchDayInput", liga1MatchDayInput);
        map.addAttribute("liga2MatchDayInput", liga2MatchDayInput);
        map.addAttribute("totoGameTypeInput", totoGameTypeInput);
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

        BankAccount customer = customerRepository.findByUserAccount(authenticationManager.getCurrentUser().get()).getAccount();
        map.addAttribute("transactions", transactionRepo.findByFromOrToOrderByDateDesc(customer, customer));// customer.getAccount().getTransactions());
        map.addAttribute("customers", customerRepository.count());

        return "statistics/overview";
    }

    private TreeMap<LottoGame, Double> createLottoOverview() {
        TreeMap<LottoGame, Double> lottoTipsMap = new TreeMap<LottoGame, Double>(comp);
        //TODO mit richtiger Zeit arbeiten lassen
        for (LottoGame lottoGame : lottoMatchRepository.findByDateAfterOrderByDateAsc(LocalDateTime.of(2014, 12, 2, 19, 0))) {
            if (!lottoTipsMap.containsKey(lottoGame))
                lottoTipsMap.put(lottoGame, 0.0D);
            
            for (LottoTip lottoTip : lottoTipRepository.findByLottoGame(lottoGame))
                lottoTipsMap.put(lottoGame, lottoTipsMap.get(lottoGame) + lottoTip.getInput());
        }

        return lottoTipsMap;
    }

    @Deprecated
    private void createTotoOverview() {
        liga1MatchDayInput = totoMatchDayOverview(TotoGameType.BUNDESLIGA1);
        liga2MatchDayInput = totoMatchDayOverview(TotoGameType.BUNDESLIGA2);
        pokalMatchDayInput = totoMatchDayOverview(TotoGameType.POKAL);

    }

    private double getTotalInput(TotoGameType totoGameType) {
        double value = 0;

        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType))
            for (TotoTip tip : totoTipRepository.findByTotoMatch(match))
                if (tip.isValid())
                    value += tip.getInput();

        return value;
    }

    @Deprecated
    private TreeMap<Integer, Double> totoMatchDayOverview(TotoGameType totoGameType) {
        TreeMap<Integer, Double> matchDayInput = new TreeMap<Integer, Double>();

        for (TotoMatch match : totoRepo.findByTotoResultAndTotoGameType(TotoResult.NOT_PLAYED, totoGameType)) {

            LocalDateTime localTime = time.getTime();
            LocalDateTime date = match.getDate();

            if (!localTime.isAfter(date)) {

                if (!totoGameTypeInput.containsKey(match.getTotoGameType())) {
                    totoGameTypeInput.put(match.getTotoGameType(), match.getTotalInput());
                }
                totoGameTypeInput.put(totoGameType, totoGameTypeInput.get(totoGameType) + match.getTotalInput());

                if (!matchDayInput.containsKey(match.getMatchDay())) {
                    matchDayInput.put(match.getMatchDay(), match.getTotalInput());
                }

                else {
                    matchDayInput.put(match.getMatchDay(), matchDayInput.get(match.getMatchDay()) + match.getTotalInput());
                }
            }
        }
        return matchDayInput;
    }
}
