package de.tudresden.swt14ws18.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.gamemanagement.TotoGameType;
import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.gamemanagement.TotoResult;

public interface TotoMatchRepository extends CrudRepository<TotoMatch, Long> {
    public TotoMatch findById(long id);

    public List<TotoMatch> findByDate(LocalDateTime date);

    public List<TotoMatch> findByTeamHomeOrTeamGuest(String teamHome, String teamGuest);

    public List<TotoMatch> findByTotoGameType(TotoGameType totoGameType);

    public List<TotoMatch> findByTotoResult(TotoResult totoResult);

    public List<TotoMatch> findByMatchDay(int matchDay);

    public List<TotoMatch> findByTotoResultAndTotoGameType(TotoResult totoResult, TotoGameType totoGameType);

    public List<TotoMatch> findByMatchDayAndTotoGameType(int matchDay, TotoGameType type);

    public List<TotoMatch> findByMatchDayAndTotoGameTypeAndTotoResult(int matchDay, TotoGameType type, TotoResult totoResult);
}
