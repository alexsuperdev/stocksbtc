package de.crazymonkey.finanzinformation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.crazymonkey.finanzinformation.entity.AktienSymbol;
import de.crazymonkey.finanzinformation.service.FinanzService;

@RestController
public class FinanzinformationController {

	@Autowired
	private FinanzService finanzService;

	@RequestMapping("/getSymbol")
	public AktienSymbol getSymbol(@RequestParam("firmName") String firmName) {
		AktienSymbol symbolForFirmname = finanzService.getSymbolForFirmname(firmName);
		return symbolForFirmname;
	}
}
