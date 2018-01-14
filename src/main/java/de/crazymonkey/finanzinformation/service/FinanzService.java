package de.crazymonkey.finanzinformation.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.crazymonkey.finanzinformation.alphavantage.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.alphavantage.entity.HistoricalData;
import de.crazymonkey.finanzinformation.annotation.LogExecutionTime;
import de.crazymonkey.finanzinformation.coindesk.entity.HistoricalDataBtc;
import de.crazymonkey.finanzinformation.constants.Endpoints;
import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.entity.Share;
import de.crazymonkey.finanzinformation.entity.Shareprice;
import de.crazymonkey.finanzinformation.exception.FinanzinformationException;
import de.crazymonkey.finanzinformation.repository.SharePriceRepository;
import de.crazymonkey.finanzinformation.repository.ShareRepository;
import de.crazymonkey.finanzinformation.util.RequestUtil;

@Service
public class FinanzService {

	@Value("${alphavantage.key}")
	private String alphavantageKey;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ShareRepository shareRepository;

	@Autowired
	private SharePriceRepository sharePriceRepository;

	@Autowired
	private RequestUtil requestUtil;

	private static int amountYearsInPast = 5;

	public AktienSymbol getSymbolForFirmname(String firmenName) {

		String urlFirmenSymbol = Endpoints.AKTIENSYMBOL.getUrl().replace("####", firmenName);
		ResponseEntity<String> response = restTemplate.getForEntity(urlFirmenSymbol, String.class);

		String serverResponse = response.getBody();
		serverResponse = serverResponse.substring(serverResponse.indexOf("symbol") - 2,
				serverResponse.indexOf("}") + 1);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			AktienSymbol aktienSymbol = mapper.readValue(serverResponse, AktienSymbol.class);
			return aktienSymbol;
		} catch (IOException e) {
			throw new FinanzinformationException(e.getMessage());
		}

	}

	@LogExecutionTime
	public Map<LocalDate, HistoricalData> getHistoricalDataAktienSymbol(String aktienSymbol) {
		ResponseEntity<String> response = requestUtil.createRequest(Endpoints.HISTORICALDATA, aktienSymbol);
		JsonNode parseResponse = requestUtil.parseResponse(response, "Time Series (Daily)");
		Map<LocalDate, HistoricalData> parseJSONFilter = convertAndFilter(parseResponse,
				LocalDate.now().minusYears(amountYearsInPast), HistoricalData.class);
		return parseJSONFilter;
	}

	@LogExecutionTime
	public List<HistoricalDataBtc> getHistoricalDataBTC(LocalDate start, LocalDate end) {
		ResponseEntity<String> response = requestUtil.createRequest(Endpoints.BTC_HIST, start, end);
		JsonNode parseResponse = requestUtil.parseResponse(response, "bpi");
		Map<LocalDate, HistoricalDataBtc> convert = convert(parseResponse, HistoricalDataBtc.class);
		List<HistoricalDataBtc> hist = new ArrayList<>();
		convert.entrySet().iterator().forEachRemaining(value -> {
			HistoricalDataBtc historicalDataBtc = value.getValue();
			historicalDataBtc.setDate(value.getKey());
			hist.add(historicalDataBtc);
		});
		return hist;
	}

	@LogExecutionTime
	public List<Shareprice> getSharePrices(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		List<Shareprice> sharePrices;
		sharePrices = getSharePricesPersist(aktienSymbol, timeTyp, amount);
		return sharePrices;
	}

	// @LogExecutionTime
	// public List<Shareprice> getSharePricesPersist(String aktienSymbol,
	// TimeSprektrum timeTyp, int amount) {
	// Map<LocalDate, HistoricalData> historicalDataAktienSymbol;
	// List<Share> shares = shareRepository.findBySymbol(aktienSymbol);
	// List<Shareprice> sharePrices;
	// if (shares.size() == 0) {
	// AktienSymbol symbolForFirmname = getSymbolForFirmname(aktienSymbol);
	// Share share = new Share();
	// share.setStock(symbolForFirmname.getIndex());
	// share.setSymbol(symbolForFirmname.getSymbol());
	// share.setSharename(symbolForFirmname.getName());
	// Share shareSaved = shareRepository.save(share);
	// historicalDataAktienSymbol = getHistoricalDataAktienSymbol(aktienSymbol);
	// sharePrices = mittelWerteErmittelnMonat(historicalDataAktienSymbol,
	// shareSaved.getId());
	// sharePriceRepository.save(sharePrices);
	// } else {
	// sharePrices = sharePriceRepository.findByShareId(shares.get(0).getId());
	// }
	// LocalDate endDate = null;
	// if (TimeSprektrum.WEEK.equals(timeTyp)) {
	// endDate = LocalDate.now().minusWeeks(amount);
	// } else if (TimeSprektrum.MONTH.equals(timeTyp)) {
	// endDate = LocalDate.now().minusMonths(amount);
	// } else if (TimeSprektrum.YEAR.equals(timeTyp)) {
	// endDate = LocalDate.now().minusYears(amount);
	// } else {
	// throw new FinanzinformationException("Timespektrum is undefined");
	// }
	// // Sehr häslich
	// final LocalDate endDateFinal = endDate;
	// List<Shareprice> sharePricesUntilDate = sharePrices.stream()
	// .filter(sharePrice ->
	// sharePrice.getPriceDate().isAfter(endDateFinal)).collect(Collectors.toList());
	// return sharePricesUntilDate;
	// }

	@Transactional
	public List<Shareprice> getSharePricesPersist(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		AktienSymbol symbolForFirmname = getSymbolForFirmname(aktienSymbol);
		Share share = new Share();
		share.setStock(symbolForFirmname.getIndex());
		share.setSymbol(symbolForFirmname.getSymbol());
		share.setSharename(symbolForFirmname.getName());
		List<Shareprice> sharePrices = getSharePricesOnDemand(aktienSymbol, timeTyp, amount);
		Share sharePersist = shareRepository.save(share);
		sharePrices.stream().forEach(sharePrice -> {
			sharePrice.setShareId(sharePersist.getId());
		});
		sharePriceRepository.save(sharePrices);
		return sharePrices;
	}

	private List<Shareprice> getSharePricesOnDemand(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		Map<LocalDate, HistoricalData> historicalDataAktienSymbol;
		historicalDataAktienSymbol = getHistoricalDataAktienSymbol(aktienSymbol);
		List<Shareprice> sharePrices = mittelWerteErmittelnMonat(historicalDataAktienSymbol, null);

		LocalDate endDate = null;
		if (TimeSprektrum.WEEK.equals(timeTyp)) {
			endDate = LocalDate.now().minusWeeks(amount);
		} else if (TimeSprektrum.MONTH.equals(timeTyp)) {
			endDate = LocalDate.now().minusMonths(amount);
		} else if (TimeSprektrum.YEAR.equals(timeTyp)) {
			endDate = LocalDate.now().minusYears(amount);
		} else {
			throw new FinanzinformationException("Timespektrum is undefined");
		}
		// Sehr häslich
		final LocalDate endDateFinal = endDate;
		List<Shareprice> sharePricesUntilDate = sharePrices.stream()
				.filter(sharePrice -> sharePrice.getPriceDate().isAfter(endDateFinal)).collect(Collectors.toList());
		return sharePricesUntilDate;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteShareData(Integer shareId) {
		sharePriceRepository.deleteByShareId(shareId);
		shareRepository.delete(shareId);
	}

	// The ENDPOINT DIGITAL_CURRENCY_DAILY was inaktivate by
	// alphavantage.co(06.01.2018)
	// @LogExecutionTime
	// public Map<LocalDate, HistoricalDataBtc> getCryptoKurse(String aktienSymbol)
	// {
	//
	// ResponseEntity<String> response = createRequest(Endpoints.BTC_DAILY,
	// aktienSymbol);
	// JsonNode parseResponse = parseResponse(response, "Time Series (Digital
	// Currency Daily)");
	// Map<LocalDate, HistoricalDataBtc> convertAndFilter =
	// convertAndFilter(parseResponse,
	// LocalDate.now().minusYears(amountYearsInPastBTC), HistoricalDataBtc.class);
	// return convertAndFilter;
	// }

	private <T> Map<LocalDate, T> convertAndFilter(JsonNode jsonNode, LocalDate filterBisDatum, Class<T> type) {
		Map<LocalDate, T> historicalData = new LinkedHashMap<LocalDate, T>();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonNode.fields().forEachRemaining(eintrag -> {
			LocalDate dateEintrag = LocalDate.parse(eintrag.getKey());
			if (filterBisDatum.isBefore(dateEintrag)) {
				historicalData.put(LocalDate.parse(eintrag.getKey()),
						(T) mapper.convertValue(eintrag.getValue(), type));
			}
		});
		return historicalData;
	}

	private <T> Map<LocalDate, T> convert(JsonNode jsonNode, Class<T> type) {
		Map<LocalDate, T> convertedResult = new LinkedHashMap<LocalDate, T>();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonNode.fields().forEachRemaining(eintrag -> {
			convertedResult.put(LocalDate.parse(eintrag.getKey()), (T) mapper.convertValue(eintrag.getValue(), type));
		});
		return convertedResult;
	}

	private List<Shareprice> mittelWerteErmittelnMonat(Map<LocalDate, HistoricalData> aktienPreise, Integer shareId) {

		List<Shareprice> sharePrices = new ArrayList<>();
		for (Entry<LocalDate, HistoricalData> entry : aktienPreise.entrySet()) {
			Shareprice sharePrice = new Shareprice();
			Float wert = (entry.getValue().getOpen() + entry.getValue().getClose()) / 2;
			sharePrice.setPrice(wert);
			sharePrice.setPriceDate(entry.getKey());
			sharePrice.setShareId(shareId);
			sharePrices.add(sharePrice);
		}
		return sharePrices;
	}

}
