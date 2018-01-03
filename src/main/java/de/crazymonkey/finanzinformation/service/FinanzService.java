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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.crazymonkey.finanzinformation.annotation.LogExecutionTime;
import de.crazymonkey.finanzinformation.constants.Endpoints;
import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.entity.HistoricalData;
import de.crazymonkey.finanzinformation.persistence.entities.Share;
import de.crazymonkey.finanzinformation.persistence.entities.Shareprice;
import de.crazymonkey.finanzinformation.repository.SharePriceRepository;
import de.crazymonkey.finanzinformation.repository.ShareRepository;

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

	private static int amountYearsInPast = 5;

	private AktienSymbol getSymbolForFirmname(String firmenName) {

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
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}

	@LogExecutionTime
	public Map<LocalDate, HistoricalData> getHistoricalDataAktienSymbol(String aktienSymbol) {

		String urlHistData = Endpoints.HISTORICALDATA.getUrl();
		urlHistData = urlHistData.replace("#symbol#", aktienSymbol);
		urlHistData = urlHistData.replace("#key#", alphavantageKey);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode rootNode = null;
		ResponseEntity<String> response = restTemplate.getForEntity(urlHistData, String.class);
		Map<LocalDate, HistoricalData> historicalData = new LinkedHashMap<LocalDate, HistoricalData>();
		try {
			rootNode = mapper.readTree(response.getBody());
			JsonNode result = rootNode.get("Time Series (Daily)");
			result.fields().forEachRemaining(eintrag -> {
				LocalDate dateEintrag = LocalDate.parse(eintrag.getKey());
				if (LocalDate.now().minusYears(amountYearsInPast).isBefore(dateEintrag)) {
					historicalData.put(LocalDate.parse(eintrag.getKey()),
							mapper.convertValue(eintrag.getValue(), HistoricalData.class));
				}
			});

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		return historicalData;
	}

	@LogExecutionTime
	public List<Shareprice> getSharePrices(String aktienSymbol, TimeSprektrum timeTyp, int amount) {
		Map<LocalDate, HistoricalData> historicalDataAktienSymbol;
		List<Share> shares = shareRepository.findBySymbol(aktienSymbol);
		List<Shareprice> sharePrices;
		if (shares.size() == 0) {
			AktienSymbol symbolForFirmname = getSymbolForFirmname(aktienSymbol);
			Share share = new Share();
			share.setStock(symbolForFirmname.getIndex());
			share.setSymbol(symbolForFirmname.getSymbol());
			share.setSharename(symbolForFirmname.getName());
			Share shareSaved = shareRepository.save(share);
			historicalDataAktienSymbol = getHistoricalDataAktienSymbol(aktienSymbol);
			sharePrices = mittelWerteErmittelnMonat(historicalDataAktienSymbol, shareSaved.getId());
			sharePriceRepository.save(sharePrices);
		} else {
			sharePrices = sharePriceRepository.findByShareId(shares.get(0).getId());
		}
		LocalDate endDate = null;
		if (TimeSprektrum.WEEK.equals(timeTyp)) {
			endDate = LocalDate.now().minusWeeks(amount);
		} else if (TimeSprektrum.MONTH.equals(timeTyp)) {
			endDate = LocalDate.now().minusMonths(amount);
		} else if (TimeSprektrum.YEAR.equals(timeTyp)) {
			endDate = LocalDate.now().minusYears(amount);
		}
		// Sehr häslich
		final LocalDate endDateFinal = endDate;
		List<Shareprice> sharePricesUntilDate = sharePrices.stream()
				.filter(sharePrice -> sharePrice.getPriceDate().isAfter(endDateFinal)).collect(Collectors.toList());
		return sharePricesUntilDate;
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
