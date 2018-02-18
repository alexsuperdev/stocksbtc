package de.crazymonkey.finanzinformation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.crazymonkey.finanzinformation.alphavantage.entity.HistoricalData;
import de.crazymonkey.finanzinformation.coindesk.entity.HistoricalDataBtc;
import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.entity.SharePrice;
import de.crazymonkey.finanzinformation.service.FinanzService;
import yahoofinance.histquotes.Interval;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
// @ContextConfiguration(classes = Application.class)
public class FinanzServiceTest {

	@Autowired
	private FinanzService finanzService;

	@Test
	public void getHistoricalDataAktienSymbol() {
		Map<LocalDate, HistoricalData> historicalDataAktienSymbol = finanzService.getHistoricalDataAktienSymbol("AAPL");
		Assert.assertNotNull(historicalDataAktienSymbol);
		Assert.assertTrue(historicalDataAktienSymbol.size() > 1);

		HistoricalData historicalData = historicalDataAktienSymbol.get(LocalDate.parse("2017-02-10"));
		Assert.assertNotNull(historicalData.getOpen());
		Assert.assertNotNull(historicalData.getHigh());
		Assert.assertNotNull(historicalData.getLow());
	}

	@Test
	public void getDataShare() {
		List<SharePrice> dataShare = finanzService.getSharePrices("MSFT", TimeSprektrum.WEEK, 1);
		Assert.assertTrue(dataShare.size() >= 2);
	}

	// @Test
	// public void getDataShareNew() {
	// List<Shareprice> dataShare = finanzService.getSharePrices("NKE",
	// TimeSprektrum.MONTH, 1);
	// Assert.assertTrue(dataShare.size() >= 0);
	//// Integer shareId = dataShare.get(0).getShareId();
	// finanzService.deleteShareData(shareId);
	// }

	@Test
	public void getHistBTC() {
		LocalDate start = LocalDate.of(2017, 5, 1);
		LocalDate end = LocalDate.of(2017, 10, 6);
		List<HistoricalDataBtc> historicalDataBTC = finanzService.getHistoricalDataBTC(start, end);
		Assert.assertEquals(159, historicalDataBTC.size());
	}

	@Test
	public void getSharePricesYahoo() {
		Calendar from = new GregorianCalendar(2018, Calendar.FEBRUARY, 10);
		Calendar to = new GregorianCalendar(2018,Calendar.FEBRUARY, 15);
		Interval interval = Interval.DAILY;
		String aktienSymbol = "TLG.F";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		System.out.println(sdf.format(to.getTime()));
		System.out.println(sdf.format(from.getTime()));
		List<SharePrice> sharePricesYahoo = finanzService.getSharePricesYahoo(aktienSymbol, interval, from, to);
		Assert.assertEquals(8, sharePricesYahoo.size());
	}

	// @Test
	// public void testYahoo() {
	// Stock test = finanzService.test();
	// System.out.println(test);
	// Assert.assertNotNull(test);
	// }
	// @Test
	// public void getCryptoKurse() {
	// Map<LocalDate, HistoricalDataBtc> cryptoKurse =
	// finanzService.getCryptoKurse("BTC");
	// Assert.assertTrue(cryptoKurse.size() >= 60);
	// }
}
