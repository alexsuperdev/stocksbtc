package de.crazymonkey.finanzinformation.util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.crazymonkey.finanzinformation.constants.Endpoints;
import de.crazymonkey.finanzinformation.exception.FinanzinformationException;

@Service
public class RequestUtil {

	@Value("${alphavantage.key}")
	private String alphavantageKey;

	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<String> createRequest(Endpoints endpoints, String aktienSymbol) {

		String urlHistData = endpoints.getUrl();
		urlHistData = urlHistData.replace("#key#", alphavantageKey);
		urlHistData = urlHistData.replace("#symbol#", aktienSymbol);
		ResponseEntity<String> response = restTemplate.getForEntity(urlHistData, String.class);
		return response;
	}

	public JsonNode parseResponse(ResponseEntity<String> response, String rootNodeName) {

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(response.getBody());
			JsonNode result = rootNode.get(rootNodeName);
			return result;

		} catch (JsonProcessingException e) {
			throw new FinanzinformationException(e.getMessage());
		} catch (IOException e) {
			throw new FinanzinformationException(e.getMessage());
		}
	}
	

	public ResponseEntity<String> createRequest(Endpoints endpoint, LocalDate start, LocalDate end) {

		String urlHistData = endpoint.getUrl();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlHistData)
				.queryParam("start", start.toString()).queryParam("end", end.toString());
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity,
				String.class);
		return response;
	}

}
