package de.tudresden.swt14ws18.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.gamemanagement.LottoGame;
import de.tudresden.swt14ws18.gamemanagement.LottoNumbers;

public interface LottoMatchRepository extends CrudRepository<LottoGame, Long> {
    public List<LottoGame> findByResultOrderByDateAsc(LottoNumbers numbers);

    public List<LottoGame> findByDateAfterOrderByDateAsc(LocalDateTime date);

    public LottoGame findByDate(LocalDateTime date);

    public List<LottoGame> findByDateBeforeOrderByDateAsc(LocalDateTime time);
}
