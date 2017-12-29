package de.crazymonkey.finanzinformation.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.persistence.entities.Shareprice;

public interface SharePriceRepository extends CrudRepository<Shareprice, Integer> {

	List<Shareprice> findByShareId(int shareId);
}
