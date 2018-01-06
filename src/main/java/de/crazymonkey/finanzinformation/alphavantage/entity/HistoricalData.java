package de.crazymonkey.finanzinformation.alphavantage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoricalData {

	@JsonProperty(value="1. open")
	private Float open;
	
	@JsonProperty(value="2. high")
	private String high;
	
	@JsonProperty(value="3. low")
	private String low;
	
	@JsonProperty(value="4. close")
	private Float close;
	
//	private String volume;
	
//	public String getOpen() {
//		return open;
//	}
//	public void setOpen(String open) {
//		this.open = open;
//	}
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
//	public String getVolume() {
//		return volume;
//	}
//	public void setVolume(String volume) {
//		this.volume = volume;
//	}
	public Float getOpen() {
		return open;
	}
	public void setOpen(Float open) {
		this.open = open;
	}
	public Float getClose() {
		return close;
	}
	public void setClose(Float close) {
		this.close = close;
	}
}
