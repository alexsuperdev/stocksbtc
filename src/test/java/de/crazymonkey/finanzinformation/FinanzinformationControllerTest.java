package de.crazymonkey.finanzinformation;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FinanzinformationControllerTest {

	@Autowired
	private MockMvc mvc;
	//
	// @Test
	// public void getSymbol() throws Exception {
	// this.mvc.perform(get("/getSymbol").with(httpBasic("financeapp",
	// "financeapp")).param("firmName", "Apple"))
	// .andExpect(status().isOk())
	// .andExpect(content().string(CoreMatchers.containsString("symbol\":\"AAPL\",\"name\":\"Apple
	// Inc.")));
	// }
	//
	// @Test
	// public void getSymbolUnauthorized() throws Exception {
	// this.mvc.perform(get("/getSymbol").with(httpBasic("financeapp1",
	// "financeapp1")).param("firmName", "Apple"))
	// .andExpect(status().is(401));
	// }

	@Test
	public void getSharePrices() throws Exception {
		this.mvc.perform(get("/getSharePrices").with(httpBasic("financeapp", "financeapp"))
				.param("aktienSymbol", "MSFT").param("timeTyp", "m").param("amount", "3")).andExpect(status().is(200));
	}

	@Test
	public void getBtcHistorical() throws Exception {
		LocalDate start = LocalDate.of(2017, 10, 1);
		LocalDate end = LocalDate.of(2017, 12, 1);
		this.mvc.perform(get("/getBtcHistorical").with(httpBasic("financeapp", "financeapp"))
				.param("start", start.toString()).param("end", end.toString())).andExpect(status().is(200));

	}
}
