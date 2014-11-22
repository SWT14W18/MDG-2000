package de.tudresden.swt14ws18.gamemanagement;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TotoMatchRepository extends CrudRepository<TotoMatch, Long>{
	public TotoMatch findById(long id);
	public List<TotoMatch> findByDate(Date date);
	public List<TotoMatch> findByTeamHomeOrTeamGuest(String teamHome, String teamGuest);
	public List<TotoMatch> findByTotoGameType(TotoGameType totoGameType);
	public List<TotoMatch> findByTotoResult(TotoResult totoResult);
}
