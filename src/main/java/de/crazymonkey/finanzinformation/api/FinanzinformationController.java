package de.crazymonkey.finanzinformation.api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.crazymonkey.finanzinformation.alphavantage.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.coindesk.entity.HistoricalDataBtc;
import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.entity.ShareMapper;
import de.crazymonkey.finanzinformation.entity.ShareValueByDate;
import de.crazymonkey.finanzinformation.entity.SharePrice;
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

	private static final Logger Logger = LoggerFactory.getLogger(FinanzinformationController.class);

	@RequestMapping(value = "/api/getSymbol", method = RequestMethod.GET)
	public AktienSymbol getSymbol(@RequestParam("firmName") String firmName) {
		AktienSymbol symbolForFirmname = finanzService.getSymbolForFirmname(firmName);
		return symbolForFirmname;
	}

	@RequestMapping(value = "/api/getSharePrices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ShareValueByDate>> getSharePrices(@RequestParam("aktienSymbol") String aktienSymbol,
			@RequestParam String timeTyp, @RequestParam int amount) {
		List<SharePrice> sharePrices = finanzService.getSharePrices(aktienSymbol, TimeSprektrum.getByValue(timeTyp),
				amount);
		List<ShareValueByDate> shareInfo = sharePrices.stream().map(ShareMapper::toShareInfo)
				.collect(Collectors.toList());
		shareInfo.stream().sorted((shareinfo1,shareinfo2) ->shareinfo1.getDatum().compareTo(shareinfo2.getDatum()));
		return ResponseEntity.ok(shareInfo);
	}

	@RequestMapping(value = "/api/getBtcHistorical", method = RequestMethod.GET)
	public List<HistoricalDataBtc> getBtcHistorical(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate end) {
		List<HistoricalDataBtc> historicalDataBTC = finanzService.getHistoricalDataBTC(start, end);
		return historicalDataBTC;
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
