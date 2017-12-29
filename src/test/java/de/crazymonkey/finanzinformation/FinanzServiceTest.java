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

import de.crazymonkey.finanzinformation.entity.HistoricalData;
import de.crazymonkey.finanzinformation.persistence.entities.Shareprice;
import de.crazymonkey.finanzinformation.service.FinanzService;

@RunWith(SpringJUnit4ClassRunner.class)
// @WebMvcTest(FinanzinformationController.class)
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
		List<Shareprice> dataShare = finanzService.getDataShare("MSFT");
		Assert.assertEquals(1260, dataShare.size());
	}
}
