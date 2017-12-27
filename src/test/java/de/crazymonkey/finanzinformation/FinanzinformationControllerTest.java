package de.crazymonkey.finanzinformation;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.crazymonkey.finanzinformation.api.FinanzinformationController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FinanzinformationController.class)
public class FinanzinformationControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getSymbol() throws Exception {
		this.mvc.perform(get("/getSymbol").with(httpBasic("financeapp", "financeapp")).param("firmName", "Apple"))
				.andExpect(status().isOk())
				.andExpect(content().string(CoreMatchers.containsString("symbol\":\"AAPL\",\"name\":\"Apple Inc.")));
	}
	
	@Test
	public void getSymbolUnauthorized() throws Exception {
		this.mvc.perform(get("/getSymbol").with(httpBasic("financeapp1", "financeapp1")).param("firmName", "Apple"))
				.andExpect(status().is(401));
	}
}
