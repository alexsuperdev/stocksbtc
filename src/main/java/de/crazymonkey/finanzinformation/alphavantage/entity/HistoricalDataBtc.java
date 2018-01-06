package de.crazymonkey.finanzinformation.alphavantage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalDataBtc {

	@JsonProperty(value = "1b. open (USD)")
	private Float open;

	@JsonProperty(value = "4b. close (USD)")
	private Float high;

	public Float getOpen() {
		return open;
	}

	public void setOpen(Float open) {
		this.open = open;
	}

	public Float getHigh() {
		return high;
	}

	public void setHigh(Float high) {
		this.high = high;
	}
}
