package de.crazymonkey.finanzinformation.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShareValueByDate {

	private LocalDate datum;
	private float preis;
	private String shareName;

	public String getDatum() {
		String format = DateTimeFormatter.ISO_DATE.format(datum);
		return format;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public float getPreis() {
		return preis;
	}

	public void setPreis(float preis) {
		this.preis = preis;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}
}
