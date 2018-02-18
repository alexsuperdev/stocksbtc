package de.crazymonkey.finanzinformation.mapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.crazymonkey.finanzinformation.entity.Share;
import de.crazymonkey.finanzinformation.entity.SharePrice;
import de.crazymonkey.finanzinformation.repository.ShareRepository;
import yahoofinance.histquotes.HistoricalQuote;

@Service
public class SharePricesMapper {

	private static final Logger LOG = LoggerFactory.getLogger(SharePricesMapper.class);

	@Autowired
	private ShareRepository shareRepository;

	public SharePrice fromYahoo(HistoricalQuote hist) {
		if (hist.getOpen() == null) {

		}
		Share aktie = shareRepository.getBySymbol(hist.getSymbol());
		SharePrice aktienPrice = new SharePrice();
		aktienPrice.setShare(aktie);
		aktienPrice.setPrice(getMittelPrice(hist.getOpen(), hist.getClose()));
		Calendar priceDateYahoo = hist.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		LOG.info(" requesting yahoo für den datum {}", sdf.format(priceDateYahoo.getTime()));

		LocalDate date = LocalDate.of(priceDateYahoo.get(Calendar.YEAR), priceDateYahoo.get(Calendar.MONTH) + 1, priceDateYahoo.get(Calendar.DAY_OF_MONTH));
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
