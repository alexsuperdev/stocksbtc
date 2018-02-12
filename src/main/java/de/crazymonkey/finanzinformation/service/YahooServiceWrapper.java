package de.crazymonkey.finanzinformation.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@Service
public class YahooServiceWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(YahooServiceWrapper.class);

	public List<HistoricalQuote> getStockHistoricalPrices(String aktienSymbol, Interval interval, Calendar from, Calendar to) {

		Stock stockInfo = null;
		List<HistoricalQuote> history = null;
		try {
			stockInfo = YahooFinance.get(aktienSymbol, from, to, interval);
			history = stockInfo.getHistory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Fehler beim Aufruf von Yahoo API {} ", e.getLocalizedMessage());
		}
		return history;
	}
}
