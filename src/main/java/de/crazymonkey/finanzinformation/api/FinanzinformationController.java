package de.crazymonkey.finanzinformation.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.crazymonkey.finanzinformation.constants.TimeSprektrum;
import de.crazymonkey.finanzinformation.persistence.entities.Shareprice;
import de.crazymonkey.finanzinformation.service.FinanzService;

@RestController
public class FinanzinformationController {

	@Autowired
	private FinanzService finanzService;

	// @RequestMapping("/getSymbol")
	// public AktienSymbol getSymbol(@RequestParam("firmName") String firmName) {
	// AktienSymbol symbolForFirmname =
	// finanzService.getSymbolForFirmname(firmName);
	// return symbolForFirmname;
	// }

	@RequestMapping("/getSharePrices")
	public List<Shareprice> getSharePrices(@RequestParam("aktienSymbol") String aktienSymbol,
			@RequestParam String timeTyp, @RequestParam int amount) {
		List<Shareprice> sharePrices = finanzService.getSharePrices(aktienSymbol, TimeSprektrum.getByValue(timeTyp),
				amount);
		return sharePrices;
	}
}
