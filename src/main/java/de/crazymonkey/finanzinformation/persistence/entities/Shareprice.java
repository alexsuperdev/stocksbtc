package de.crazymonkey.finanzinformation.persistence.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Shareprice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private float price;

	@Column(name = "pricedate", columnDefinition = "DATE")
	private LocalDate priceDate;

	@Column(name = "created")
	@CreationTimestamp
	private Timestamp created;

	@Column(name = "shareid")
	@NotNull
	private Integer shareId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Integer getShareId() {
		return shareId;
	}

	public void setShareId(Integer shareId) {
		this.shareId = shareId;
	}

	public LocalDate getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(LocalDate priceDate) {
		this.priceDate = priceDate;
	}

}
