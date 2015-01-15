package de.tudresden.swt14ws18.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

/**
 * Müsste eigentlich in andere Controller aufgeteilt werden, aber never touch a running system.
 */
@Controller
@PreAuthorize("hasRole('ROLE_TOTOLIST')")
public class TotoListController extends ControllerBase {

    @Autowired
    public TotoListController(UserAccountManager userAccountManager, CustomerRepository customerRepository, CommunityRepository communityRepository,
            AuthenticationManager authenticationManager, BankAccountRepository bankAccountRepository, TotoMatchRepository totoRepo,
            LottoTipCollectionRepository lottoTipCollectionRepo, TotoTipCollectionRepository totoTipCollectionRepo, BusinessTime time,
            TotoTipRepository totoTipRepository, LottoMatchRepository lottoMatchRepository, LottoTipRepository lottoTipRepository,
            TotoMatchRepository totoMatchRepository, MessageRepository messageRepo, TipFactory tipFactory, TransactionRepository transactionRepo) {
        super(userAccountManager, customerRepository, communityRepository, authenticationManager, bankAccountRepository, totoRepo,
                lottoTipCollectionRepo, totoTipCollectionRepo, time, totoTipRepository, lottoMatchRepository, lottoTipRepository,
                totoMatchRepository, messageRepo, tipFactory, transactionRepo);
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
        for (Role r : it) {
            if (r.toString().equals("ROLE_BOSS")) {
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
            if (!match.isFinished() && match.getDate().isBefore(time.getTime())) {
                set.add(match.getMatchDay());
            }
        }
        map.addAttribute("matchDays", set);
        map.addAttribute("liga", totoGameType.name());
        map.addAttribute("totoGameType", totoGameType.toString());
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for (Role r : it) {
            if (r.toString().equals("ROLE_BOSS")) {
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
        for (Role r : it) {
            if (r.toString().equals("ROLE_BOSS")) {
                return "admin/lotterydrawsettoto";
            }
        }
        return "games/totoTipp";
    }

    @RequestMapping("/ADMINtotoTipp")
    public String admintotoTipp(@RequestParam("liga") String liga, @RequestParam("id") int id, ModelMap map) throws IOException {
        handleGeneralValues(map);

        TotoGameType totoGameType = TotoGameType.valueOf(liga);
        List<Entry<TotoMatch, TotoResult>> list = new ArrayList<>();

        for (Entry<TotoMatch, TotoResult> match : updateTotoResultMatchDay(id, totoGameType).entrySet()) {

            if (!match.getKey().isFinished() && match.getKey().getDate().isBefore(time.getTime())) {
                list.add(match);
            }
        }

        map.addAttribute("matches", list);
        map.addAttribute("totoGameType", totoGameType.toString());
        map.addAttribute("matchDay", id);
        map.addAttribute("groups", communityRepository.findByMembers(getCurrentUser()));
        Iterable<Role> it = authenticationManager.getCurrentUser().get().getRoles();
        for (Role r : it) {
            if (r.toString().equals("ROLE_BOSS")) {
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
        for (Role r : it) {
            if (r.toString().equals("ROLE_BOSS")) {
                return "admin/lotterydrawtoto";
            }
        }
        return "games/toto";
    }

    public Map<TotoMatch, TotoResult> updateTotoResultMatchDay(int matchDay, TotoGameType totoGameType) throws IOException {

        Map<TotoMatch, TotoResult> resultMap = new HashMap<>();

        String leagueString = "";
        switch (totoGameType) {
        case BUNDESLIGA1:
            leagueString = "bl1";
            break;
        case BUNDESLIGA2:
            leagueString = "bl2";
            break;
        case POKAL:
            leagueString = "dfb2014nf";
            break;
        }

        URL url = new URL("http://openligadb-json.herokuapp.com/api/matchdata_by_group_league_saison?group_order_id=" + matchDay
                + "&league_saison=2014&league_shortcut=" + leagueString);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        JsonArray matches = (JsonArray) rootobj.get("matchdata");

        for (TotoMatch totoMatch : totoMatchRepository.findByMatchDayAndTotoGameType(matchDay, totoGameType)) {

            for (int i = 0; i < matches.size(); i++) {
                JsonObject match = (JsonObject) matches.get(i);
                if (totoMatch.getJsonMatchId() == match.get("match_id").getAsInt()) {
                    if (match.get("match_is_finished").getAsBoolean()) {
                        int scoreHome = match.get("points_team1").getAsInt();
                        int scoreGuest = match.get("points_team2").getAsInt();
                        totoMatch.setScoreHome(scoreHome);
                        totoMatch.setScoreGuest(scoreGuest);
                        TotoResult result = TotoResult.NOT_PLAYED;
                        switch (scoreHome > scoreGuest ? +1 : scoreHome < scoreGuest ? -1 : 0) {
                        case -1:
                            result = TotoResult.WIN_GUEST;
                            break;
                        case 0:
                            result = TotoResult.DRAW;
                            break;
                        case 1:
                            result = TotoResult.WIN_HOME;
                            break;
                        }
                        resultMap.put(totoMatch, result);
                    }
                }
            }
        }
        return resultMap;
    }

}
