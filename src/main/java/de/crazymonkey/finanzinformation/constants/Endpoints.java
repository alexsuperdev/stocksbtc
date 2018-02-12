package de.crazymonkey.finanzinformation.constants;

public enum Endpoints {

	AKTIENSYMBOL(
			"http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=####&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback"),
	HISTORICALDATA(
			"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=#symbol#&outputsize=full&apikey=#key#"),
	BTC_INTRADAY(
			"https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_INTRADAY&symbol=BTC&market=CNY&apikey=#key#"),
	BTC_HIST(
			"https://api.coindesk.com/v1/bpi/historical/close.json"),
	NEWSZEIT(
			"http://api.zeit.de/keyword?q=*###*&limit=1");
	
	private String url;

	public String getUrl() {
		return url;
	}

	Endpoints(String url) {
		this.url = url;
	}
}
