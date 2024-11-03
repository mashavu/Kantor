package main.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import main.model.CurrencyData;

@FeignClient(name = "nbpApi", url = "${nbp.api.url}")
public interface CurrencyDataProxy {

	@GetMapping("/{currencyCode}")
	CurrencyData getCurrencyData(@PathVariable String currencyCode);
}