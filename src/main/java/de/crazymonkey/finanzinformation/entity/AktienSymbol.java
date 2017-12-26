package de.crazymonkey.finanzinformation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AktienSymbol {

	private String symbol;

	private String name;

	@JsonIgnoreProperties
	private String exch;

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
}
