package de.crazymonkey.finanzinformation.repository;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.entity.Share;

public interface ShareRepository extends CrudRepository<Share, Integer> {

	public Share getBySymbol(String symbol);
}
