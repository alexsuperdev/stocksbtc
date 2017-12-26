package de.crazymonkey.finanzinformation.constants;

public enum Endpoints {

	AKTIENSYMBOL(
			"http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=####&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback");

	private String url;

	public String getUrl() {
		return url;
	}

	Endpoints(String url) {
		this.url = url;
	}
}
