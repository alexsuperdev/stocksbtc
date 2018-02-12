package de.crazymonkey.finanzinformation.coindesk.entity;

import java.time.LocalDate;

public class HistoricalDataBtc {

	public HistoricalDataBtc(double price) {
		super();
		this.price = price;
	}

	public HistoricalDataBtc(int price) {
		super();
		this.price = price;
	}

	private double price;

	private LocalDate date;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
