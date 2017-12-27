package de.crazymonkey.finanzinformation.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import de.crazymonkey.finanzinformation.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.entity.HistoricalData;

@Service
public class FinanzService {

	@Value("${alphavantage.key}")
	private String alphavantageKey;

	@Autowired
	private RestTemplate restTemplate;

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
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}

	}

	@LogExecutionTime
	public List<HistoricalData> getHistoricalDataAktienSymbol(String aktienSymbol) {

		String urlHistData = Endpoints.HISTORICALDATA.getUrl();
		urlHistData = urlHistData.replace("#symbol#", aktienSymbol);
		urlHistData = urlHistData.replace("#key#", alphavantageKey);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode rootNode = null;
		ResponseEntity<String> response = restTemplate.getForEntity(urlHistData, String.class);
		List<HistoricalData> historicalDatas = new ArrayList<>();
		try {
			rootNode = mapper.readTree(response.getBody());
			JsonNode result = rootNode.get("Time Series (Daily)");
			result.fields().forEachRemaining(
					eintrag -> historicalDatas.add(mapper.convertValue(eintrag.getValue(), HistoricalData.class)));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		return historicalDatas;
	}

}
