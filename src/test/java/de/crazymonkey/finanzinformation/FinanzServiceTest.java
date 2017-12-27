package de.crazymonkey.finanzinformation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.crazymonkey.finanzinformation.api.FinanzinformationController;
import de.crazymonkey.finanzinformation.entity.HistoricalData;
import de.crazymonkey.finanzinformation.service.FinanzService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FinanzinformationController.class)
public class FinanzServiceTest {

	@Autowired
	private FinanzService finanzService;

	@Test
	public void getHistoricalDataAktienSymbol() {
		List<HistoricalData> historicalDataAktienSymbol = finanzService.getHistoricalDataAktienSymbol("GOOG");
		Assert.assertNotNull(historicalDataAktienSymbol);
		Assert.assertTrue(historicalDataAktienSymbol.size() > 1);

		HistoricalData historicalData = historicalDataAktienSymbol.get(0);
		Assert.assertNotNull(historicalData.getOpen());
		Assert.assertNotNull(historicalData.getHigh());
		Assert.assertNotNull(historicalData.getLow());
	}

}
