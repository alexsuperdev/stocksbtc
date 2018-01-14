package de.crazymonkey.finanzinformation.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.crazymonkey.finanzinformation.alphavantage.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.coindesk.entity.HistoricalDataBtc;
import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.entity.Shareprice;
import de.crazymonkey.finanzinformation.service.FinanzService;

/**
 * Public API for call from Front End. All request should have basicAuth
 * according security.userrole and security.username in the
 * application.properties
 * 
 * @author Алексей
 *
 */
@RestController
public class FinanzinformationController {

	@Autowired
	private FinanzService finanzService;

	@RequestMapping("/api/getSymbol")
	public AktienSymbol getSymbol(@RequestParam("firmName") String firmName) {
		AktienSymbol symbolForFirmname = finanzService.getSymbolForFirmname(firmName);
		return symbolForFirmname;
	}

	@RequestMapping("/api/getSharePrices")
	public List<Shareprice> getSharePrices(@RequestParam("aktienSymbol") String aktienSymbol,
			@RequestParam String timeTyp, @RequestParam int amount) {
		List<Shareprice> sharePrices = finanzService.getSharePrices(aktienSymbol, TimeSprektrum.getByValue(timeTyp),
				amount);
		return sharePrices;
	}

	@RequestMapping("/api/getBtcHistorical")
	public List<HistoricalDataBtc> getBtcHistorical(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate end) {
		List<HistoricalDataBtc> historicalDataBTC = finanzService.getHistoricalDataBTC(start, end);
		return historicalDataBTC;
	}
}
