package de.crazymonkey.finanzinformation.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.crazymonkey.finanzinformation.entity.SharePrice;

public interface SharePriceRepository extends CrudRepository<SharePrice, Integer> {

	void deleteByShareId(int shareId);
}
