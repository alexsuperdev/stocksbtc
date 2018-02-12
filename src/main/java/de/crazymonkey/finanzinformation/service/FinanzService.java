package de.crazymonkey.finanzinformation.service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
import de.crazymonkey.finanzinformation.entity.SharePrice;
import de.crazymonkey.finanzinformation.exception.FinanzinformationException;
import de.crazymonkey.finanzinformation.mapper.SharePricesMapper;
import de.crazymonkey.finanzinformation.repository.SharePriceRepository;
import de.crazymonkey.finanzinformation.repository.ShareRepository;
import de.crazymonkey.finanzinformation.util.RequestUtil;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

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
	private SharePricesMapper sharePricesMapper;

	@Autowired
	private YahooServiceWrapper yahooServiceWrapper;

	@Autowired
	private RequestUtil requestUtil;

	private static int amountYearsInPast = 1;

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
	public List<SharePrice> getSharePrices(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		List<SharePrice> sharePrices;
		sharePrices = getSharePricesPersist(aktienSymbol, timeTyp, amount);
		return sharePrices;
	}

	@LogExecutionTime
	public List<SharePrice> getSharePricesYahoo(String aktienSymbol, Interval interval, Calendar from, Calendar to) {

		Share aktie = shareRepository.getBySymbol(aktienSymbol);
		Pair<LocalDate, List<SharePrice>> ermittleDatumNoDataFor = ermittleDatumNoDataFor(aktie.getSharePrices(), from,
				to);
		LocalDate dateFromNoData = ermittleDatumNoDataFor.getFirst();
		LocalDate dateTO = LocalDate.of(to.get(Calendar.YEAR), to.get(Calendar.MONTH) + 1,
				to.get(Calendar.DAY_OF_MONTH));
		if (dateFromNoData.isEqual(dateTO)) {
			return ermittleDatumNoDataFor.getSecond();
		} else {
			Calendar newFrom = new GregorianCalendar(dateFromNoData.getYear(), dateFromNoData.getMonthValue(),
					dateFromNoData.getDayOfMonth());
			List<HistoricalQuote> stockHistoricalPrices = yahooServiceWrapper.getStockHistoricalPrices(aktienSymbol,
					interval, newFrom, to);
			List<SharePrice> sharePriceYahoo = stockHistoricalPrices.stream().map(sharePricesMapper::fromYahoo)
					.collect(Collectors.toList());
			aktie.getSharePrices().addAll(sharePriceYahoo);
			shareRepository.save(aktie);
			sharePriceYahoo.addAll(ermittleDatumNoDataFor.getSecond());
			return sharePriceYahoo;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<SharePrice> getSharePricesPersist(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		Share share = shareRepository.getBySymbol(aktienSymbol);
		List<SharePrice> sharePricesOld = new ArrayList<>();
		if (share != null) {
			sharePricesOld = share.getSharePrices();
		} else {
			share = new Share();
			AktienSymbol symbolForFirmname = getSymbolForFirmname(aktienSymbol);
			share.setStock(symbolForFirmname.getIndex());
			share.setSymbol(symbolForFirmname.getSymbol());
			share.setSharename(symbolForFirmname.getName());
		}
		LocalDate ermittleDifferent = ermittleDatumNoDataFor(sharePricesOld, timeTyp, amount);
		List<SharePrice> sharePrices = new ArrayList<>();
		if (ermittleDifferent.isBefore(LocalDate.now())) {
			sharePrices = getSharePricesOnDemand(aktienSymbol, ermittleDifferent);
			if (!CollectionUtils.isEmpty(sharePrices)) {
				share.getSharePrices().addAll(sharePrices);
				shareRepository.save(share);
				return sharePrices;
			}
		}

		return sharePricesOld;
	}

	private LocalDate ermittleDatumNoDataFor(List<SharePrice> sharePricesOld, TimeSprektrum timeTyp, int amount) {
		LocalDate beginDate = getBeginDate(timeTyp, amount);
		for (SharePrice sharePrice : sharePricesOld) {
			if (sharePrice.getPriceDate().isAfter(beginDate)) {
				beginDate = sharePrice.getPriceDate().plusDays(1);
				if (!(beginDate.getDayOfWeek() == DayOfWeek.SATURDAY || beginDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
					beginDate.plusDays(1);
				}
			}
		}

		return beginDate;
	}

	private Pair<LocalDate, List<SharePrice>> ermittleDatumNoDataFor(List<SharePrice> sharePricesOld, Calendar from,
			Calendar to) {
		LocalDate dateFrom = LocalDate.of(from.get(Calendar.YEAR), from.get(Calendar.MONTH) + 1,
				from.get(Calendar.DAY_OF_MONTH));
		LocalDate dateTo = LocalDate.of(to.get(Calendar.YEAR), to.get(Calendar.MONTH) + 1,
				to.get(Calendar.DAY_OF_MONTH));
		Stream<SharePrice> sharePricesInRange = sharePricesOld.stream().filter(
				share -> share.getPriceDate().compareTo(dateFrom) > 0 && share.getPriceDate().compareTo(dateTo) <= 0);

		sharePricesInRange.forEachOrdered(sharePrice -> {
			if (sharePrice.getPriceDate().compareTo(dateFrom) == 0) {
				if (!(dateFrom.getDayOfWeek() == DayOfWeek.SATURDAY || dateFrom.getDayOfWeek() == DayOfWeek.SUNDAY)) {
					dateFrom.plusDays(1);
				}
			}
		});
		return Pair.of(dateFrom, sharePricesInRange.collect(Collectors.toList()));
	}

	private LocalDate getBeginDate(TimeSprektrum timeTyp, int amount) {
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

		return endDate;
	}

	private List<SharePrice> getSharePricesOnDemand(String aktienSymbol, LocalDate ermittleDifferent) {
		Map<LocalDate, HistoricalData> historicalDataAktienSymbol;
		historicalDataAktienSymbol = getHistoricalDataAktienSymbol(aktienSymbol);
		Share findBySymbol = shareRepository.getBySymbol(aktienSymbol);
		List<SharePrice> sharePrices = mittelWerteErmittelnMonat(historicalDataAktienSymbol, findBySymbol);

		List<SharePrice> sharePricesUntilDate = sharePrices.stream()
				.filter(sharePrice -> sharePrice.getPriceDate().isAfter(ermittleDifferent))
				.collect(Collectors.toList());
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

	private List<SharePrice> mittelWerteErmittelnMonat(Map<LocalDate, HistoricalData> aktienPreise, Share share) {

		List<SharePrice> sharePrices = new ArrayList<>();
		for (Entry<LocalDate, HistoricalData> entry : aktienPreise.entrySet()) {
			SharePrice sharePrice = new SharePrice();
			Float wert = (entry.getValue().getOpen() + entry.getValue().getClose()) / 2;
			sharePrice.setPrice(wert);
			sharePrice.setPriceDate(entry.getKey());
			sharePrice.setShare(share);
			sharePrices.add(sharePrice);
		}
		return sharePrices;
	}

}
