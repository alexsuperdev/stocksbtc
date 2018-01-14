package de.crazymonkey.finanzinformation.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.entity.Shareprice;

public interface SharePriceRepository extends CrudRepository<Shareprice, Integer> {

	List<Shareprice> findByShareId(int shareId);
	
	void deleteByShareId(int shareId);
}
