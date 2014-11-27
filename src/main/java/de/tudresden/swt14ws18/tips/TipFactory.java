package de.tudresden.swt14ws18.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;
import de.tudresden.swt14ws18.repositories.LottoMatchRepository;
import de.tudresden.swt14ws18.repositories.TotoMatchRepository;
import de.tudresden.swt14ws18.useraccountmanager.ConcreteCustomer;

@Component
public class TipFactory {
    private static final int LOTTO_TIPS_PER_PAGE = 6;

    private LottoMatchRepository lottoMatchRepository;
    private TotoMatchRepository totoMatchRepository;

    @Autowired
    public TipFactory(LottoMatchRepository lottoMatchRepository, TotoMatchRepository totoMatchRepository) {
	this.lottoMatchRepository = lottoMatchRepository;
	this.totoMatchRepository = totoMatchRepository;
    }

    public TipCollection craftTips(Map<String, String> map, ConcreteCustomer owner) {
	switch (map.get("gameType")) {
	case "lotto":
	    return craftLottoTips(map, owner);
	case "toto":
	    return craftTotoTips(map, owner);
	default:
	    return null;
	}
    }

    private TipCollection craftTotoTips(Map<String, String> map, ConcreteCustomer owner) {

	List<Tip> tips = new ArrayList<>();

	for (TotoMatch totoMatch : totoMatchRepository.findByTotoResult(TotoResult.NOT_PLAYED)) {
	    if (totoMatch.isFinished())
		continue;

	    if (map.containsKey(String.valueOf(totoMatch.getId()))) {
		TotoResult result = TotoResult.parseString(map.get(String.valueOf(totoMatch.getId())));

		tips.add(new TotoTip(totoMatch, result, 1)); // TODO define input per user
	    }

	}

	return new TipCollection(tips, owner);
    }

    private TipCollection craftLottoTips(Map<String, String> map, ConcreteCustomer owner) {

	List<Tip> tips = new ArrayList<>();
	List<LottoNumbers> numbers = new ArrayList<>();

	for (int i = 1; i <= LOTTO_TIPS_PER_PAGE; i++) {
	    if (!isValidLottoTip(map, i))
		continue;

	    // yes, this is control flow with exceptions - I already feel bad...
	    try {
		int n1 = Integer.parseInt(map.get("number" + i + "-1"));
		int n2 = Integer.parseInt(map.get("number" + i + "-2"));
		int n3 = Integer.parseInt(map.get("number" + i + "-3"));
		int n4 = Integer.parseInt(map.get("number" + i + "-4"));
		int n5 = Integer.parseInt(map.get("number" + i + "-5"));
		int n6 = Integer.parseInt(map.get("number" + i + "-6"));
		int nsuper = Integer.parseInt(map.get("number" + i + "-super"));

		numbers.add(new LottoNumbers(nsuper, n1, n2, n3, n4, n5, n6));
	    } catch (NumberFormatException e) {
		continue;
	    } catch (IllegalArgumentException ex) {
		continue;
	    }
	}

	int games = 0;
	switch (map.get("duration")) {
	case "0":
	    games = 1;
	    break;
	case "1":
	    games = 4;
	    break;
	case "2":
	    games = 24;
	    break;
	case "3":
	    games = 48;
	    break;
	default:
	    return null;
	}

	List<LottoGame> g = lottoMatchRepository.findByResultOrderByDateAsc(null);
	for (LottoNumbers num : numbers)
	    for (int i = 0; i < games; i++)
		tips.add(new LottoTip((LottoGame) g.get(i), num));

	return new TipCollection(tips, owner);
    }

    /**
     * Oh great flying spaghetti monster, forgive me for this code
     * 
     * @param map
     *            HTTP parameter Map
     * @param i
     *            der i-te Tip in dem Formular
     * @return true wenn der Tipp geht, false wenn nicht
     */
    private boolean isValidLottoTip(Map<String, String> map, int i) {
	if (!map.containsKey("number" + i + "-1"))
	    return false;

	if (!map.containsKey("number" + i + "-2"))
	    return false;

	if (!map.containsKey("number" + i + "-3"))
	    return false;

	if (!map.containsKey("number" + i + "-4"))
	    return false;

	if (!map.containsKey("number" + i + "-5"))
	    return false;

	if (!map.containsKey("number" + i + "-6"))
	    return false;

	if (!map.containsKey("number" + i + "-super"))
	    return false;

	return true;
    }
}
