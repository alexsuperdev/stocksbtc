package de.crazymonkey.finanzinformation;

import org.junit.Assert;
import org.junit.Test;

import de.crazymonkey.finanzinformation.utils.RequestUtils;

public class RequestUtilsTest {

	@Test
	public void getRequest() {
		String url = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=apple&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
		String response = RequestUtils.get(url);
		Assert.assertTrue(response.contains("\"symbol\":\"AAPL\""));
	}
}
