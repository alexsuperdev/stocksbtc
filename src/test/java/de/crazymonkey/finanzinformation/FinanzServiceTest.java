package de.crazymonkey.finanzinformation;

import java.time.LocalDate;
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
import de.crazymonkey.finanzinformation.entity.Shareprice;
import de.crazymonkey.finanzinformation.service.FinanzService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FinanzServiceTest {

	@Autowired
	private FinanzService finanzService;

	@Test
	public void getHistoricalDataAktienSymbol() {
		Map<LocalDate, HistoricalData> historicalDataAktienSymbol = finanzService.getHistoricalDataAktienSymbol("AAPL");
		Assert.assertNotNull(historicalDataAktienSymbol);
		Assert.assertTrue(historicalDataAktienSymbol.size() > 1);

		HistoricalData historicalData = historicalDataAktienSymbol.get(LocalDate.parse("2016-01-27"));
		Assert.assertNotNull(historicalData.getOpen());
		Assert.assertNotNull(historicalData.getHigh());
		Assert.assertNotNull(historicalData.getLow());
	}

	@Test
	public void getDataShare() {
		List<Shareprice> dataShare = finanzService.getSharePrices("MSFT", TimeSprektrum.MONTH, 3);
		Assert.assertTrue(dataShare.size() >= 50);
	}

	@Test
	public void getHistBTC() {
		LocalDate start = LocalDate.of(2017, 5, 1);
		LocalDate end = LocalDate.of(2017, 10, 6);
		List<HistoricalDataBtc> historicalDataBTC = finanzService.getHistoricalDataBTC(start, end);
		Assert.assertEquals(159, historicalDataBTC.size());
	}

	// @Test
	// public void getCryptoKurse() {
	// Map<LocalDate, HistoricalDataBtc> cryptoKurse =
	// finanzService.getCryptoKurse("BTC");
	// Assert.assertTrue(cryptoKurse.size() >= 60);
	// }
}
