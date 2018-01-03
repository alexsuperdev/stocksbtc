package de.crazymonkey.finanzinformation.constants;

public enum TimeSprektrum {

	WEEK("w"), MONTH("m"), YEAR("y");

	private String value;

	public String getValue() {
		return value;
	}

	TimeSprektrum(String value) {
		this.value = value;
	}

	public static TimeSprektrum getByValue(String value) {
		for (TimeSprektrum timeSprektrum : values()) {
			if (timeSprektrum.getValue().equals(value)) {
				return timeSprektrum;
			}
		}
		return null;
	}
}
