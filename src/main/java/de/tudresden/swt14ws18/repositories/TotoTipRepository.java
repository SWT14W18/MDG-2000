package de.tudresden.swt14ws18.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.gamemanagement.TotoMatch;
import de.tudresden.swt14ws18.tips.TotoTip;

public interface TotoTipRepository extends CrudRepository<TotoTip, Long> {
	public List<TotoTip> findByTotoMatch(TotoMatch totoMatch);
	
}
