package de.tudresden.swt14ws18.gamemanagement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LottoMatchRepository extends CrudRepository<LottoGame, Long>{
    public List<LottoGame> findByResultOrderByDateAsc(LottoNumbers numbers);
}
