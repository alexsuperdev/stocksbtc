package de.crazymonkey.finanzinformation.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "shareprice")
public class SharePrice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private float price;

	@Column(name = "pricedate", columnDefinition = "DATE")
	private LocalDate priceDate;

	@Column(name = "created")
	@CreationTimestamp
	private Timestamp created;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shareid", nullable = false)
	private Share share;

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

	public LocalDate getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(LocalDate priceDate) {
		this.priceDate = priceDate;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

}
