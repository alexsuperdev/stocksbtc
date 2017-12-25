package finanzinformation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.crazymonkey.finanzinformation.utils.RequestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration
public class RequestUtilsTest {

	@Test
	public void getRequest() {
		String url = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=basf&region=US&lang=en-US&row=ALL&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
		String httpEntity = RequestUtils.get(url);
		Assert.assertEquals("ResultSet", httpEntity);

	}
}
