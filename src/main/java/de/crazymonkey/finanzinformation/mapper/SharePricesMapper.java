package de.crazymonkey.finanzinformation.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.crazymonkey.finanzinformation.entity.Share;
import de.crazymonkey.finanzinformation.entity.SharePrice;
import de.crazymonkey.finanzinformation.repository.ShareRepository;
import yahoofinance.histquotes.HistoricalQuote;

@Service
public class SharePricesMapper {

	@Autowired
	private ShareRepository shareRepository;

	public SharePrice fromYahoo(HistoricalQuote hist) {
		Share aktie = shareRepository.getBySymbol(hist.getSymbol());
		SharePrice aktienPrice = new SharePrice();
		aktienPrice.setShare(aktie);
		aktienPrice.setPrice(getMittelPrice(hist.getOpen(), hist.getClose()));
		Calendar date2 = hist.getDate();
		LocalDate date = LocalDate.of(date2.get(Calendar.YEAR), date2.get(Calendar.MONTH) + 1,
				date2.get(Calendar.DAY_OF_MONTH));
		aktienPrice.setPriceDate(date);
		// SharePriceMainData priceMainData = new SharePriceMainData();
		// priceMainData.setPrice(getMittelPrice(hist.getOpen(),hist.getClose()));
		// priceMainData.setShareName(aktie.getSharename());
		// priceMainData.setTimeValue(aktie.getSharename());
		return aktienPrice;
	}

	private static float getMittelPrice(BigDecimal price1, BigDecimal price2) {
		BigDecimal result = price1.add(price2).divide(BigDecimal.valueOf(2l));
		return result.floatValue();
	}
}
