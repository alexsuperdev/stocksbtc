package de.crazymonkey.finanzinformation.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.persistence.entities.Share;

public interface ShareRepository extends CrudRepository<Share, Long> {

	public List<Share> findBySymbol(String symbol);
}
