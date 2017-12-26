package de.crazymonkey.finanzinformation.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.crazymonkey.finanzinformation.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.utils.RequestUtils;

@Service
public class FinanzService {

	private static String endPointSymbol = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=####&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback";

	public void getFirmdataForCondition() {

	}

	private AktienSymbol getSymbolForFirmname(String firmenName) {

		String urlFirmenSymbol = endPointSymbol.replace("####", firmenName);
		String serverResponse = RequestUtils.get(urlFirmenSymbol);
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

}
