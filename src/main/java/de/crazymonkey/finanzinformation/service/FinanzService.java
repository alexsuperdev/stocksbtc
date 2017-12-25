package de.crazymonkey.finanzinformation.service;

import org.springframework.stereotype.Service;

import de.crazymonkey.finanzinformation.utils.RequestUtils;

@Service
public class FinanzService {

	private static String endPointSymbol = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=####&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback";

	public void getFirmdataForCondition() {

	}

	private String getSymbolForFirmname(String firmenName) {

		String urlFirmenSymbol = endPointSymbol.replace("####", firmenName);
		String text = RequestUtils.get(urlFirmenSymbol);
		;
		// $reqString =
		// "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=####&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
		// $firma = str_replace(" ", "%20", $firma);
		// $reqString = str_replace("####", $firma, $reqString);
		// $responseSer = file_get_contents(($reqString));
		// $cleanResp = substr($responseSer, strpos($responseSer, "({") + 1,
		// strlen($responseSer) - (strpos($responseSer, "({") + 3));
		// $resArr = json_decode($cleanResp, true);
		// if (isset($resArr["ResultSet"]["Result"][0]["symbol"])) {
		// return $resArr["ResultSet"]["Result"][0]["symbol"];
		// } else {
		// return null;
		// }
		return text;
	}
}
