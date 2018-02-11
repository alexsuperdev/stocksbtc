package de.crazymonkey.finanzinformation.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Share {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String sharename;

	private String symbol;

	// index kann nicht vergeben werden, da index ist in SQL ein reserviertes Wort
	private String stock;

	@Column(name = "created")
	@CreationTimestamp
	private Timestamp created;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "share")
	private List<SharePrice> sharePrices;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSharename() {
		return sharename;
	}

	public void setSharename(String sharename) {
		this.sharename = sharename;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public List<SharePrice> getSharePrices() {
		return sharePrices;
	}

	public void setSharePrices(List<SharePrice> sharePrices) {
		this.sharePrices = sharePrices;
	}

}
