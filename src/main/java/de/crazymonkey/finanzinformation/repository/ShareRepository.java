package de.crazymonkey.finanzinformation.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.entity.Share;

public interface ShareRepository extends CrudRepository<Share, Integer> {

	public List<Share> findBySymbol(String symbol);
}
