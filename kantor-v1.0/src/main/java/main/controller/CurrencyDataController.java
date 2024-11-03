package main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import main.model.CurrencyData;
import main.proxy.CurrencyDataProxy;

@RestController
public class CurrencyDataController {

	private final CurrencyDataProxy currencyDataProxy;

	public CurrencyDataController(CurrencyDataProxy currencyDataProxy) {
		this.currencyDataProxy = currencyDataProxy;
	}

	@GetMapping("/{currencyCode}")
	public CurrencyData getCurrencyData(@PathVariable String currencyCode) {
		return currencyDataProxy.getCurrencyData(currencyCode);
	}
}