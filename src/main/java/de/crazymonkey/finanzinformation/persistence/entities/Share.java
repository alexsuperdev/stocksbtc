package de.crazymonkey.finanzinformation.persistence.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	// public LocalDateTime getCreated() {
	// return created;
	// }
	//
	// public void setCreated(LocalDateTime created) {
	// this.created = created;
	// }

}
