package de.crazymonkey.finanzinformation.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalData {

	@JsonProperty(value="1. open")
	private String open;
	
	@JsonProperty(value="2. high")
	private String high;
	
	@JsonProperty(value="3. low")
	private String low;
	
	@JsonProperty(value="4. close")
	private String close;
	private String volume;
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
}
