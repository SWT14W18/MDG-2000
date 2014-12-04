package de.tudresden.swt14ws18.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.tips.LottoTip;

public interface LottoTipRepository extends CrudRepository<LottoTip, Long> {
	public List<LottoTip> findByLottoGame(LottoGame game);

}
