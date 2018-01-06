package de.crazymonkey.finanzinformation.alphavantage.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AktienSymbol {

	private String symbol;

	private String name;

	// @JsonIgnoreProperties
	@JsonProperty(value = "exch")
	private String index;

	@JsonIgnoreProperties
	private String type;

	@JsonIgnoreProperties
	private String exchDisp;

	@JsonIgnoreProperties
	private String typeDisp;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
