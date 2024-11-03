package main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import main.model.CurrencyData;
import main.model.ExchangeOrder;
import main.model.Rate;

@Controller
public class HomeController {

	private final CurrencyDataController currencyDataController;

	public HomeController(CurrencyDataController currencyDataController) {
		this.currencyDataController = currencyDataController;
	}
	
	@GetMapping("/")
    public String home(Model model) {
        model.addAttribute("exchangeOrder", new ExchangeOrder());
        model.addAttribute("currencyTypeList", createCurrencyTypeList());
        return "home";
    }
	
	@PostMapping("/exchangeOrder")
    public String addContent(@Valid @ModelAttribute ExchangeOrder exchangeOrder, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("currencyTypeList", createCurrencyTypeList());
            return "home";
        }
        String result = getCurrencyExchangeResult(exchangeOrder);
        model.addAttribute("exchangeOrder", new ExchangeOrder());
        model.addAttribute("currencyTypeList", createCurrencyTypeList());
        model.addAttribute("result", result);
        return "home";
    }
	
	List<String> createCurrencyTypeList() {
		List<String> list = new ArrayList<>(Arrays.asList("wybierz walutę",
														  "dolar amerykański",
														  "dolar australijski",
														  "dolar kanadyjski",
														  "euro",
														  "forint",
														  "frank szwajcarski",
														  "funt szterling",
														  "jen",
														  "korona czeska",
														  "korona duńska",
														  "korona norweska",
														  "korona szwedzka",
														  "polski złoty"));
		return list;
	}
	
	String getCurrencyExchangeResult(ExchangeOrder exchangeOrder) {
		double inputAmount = exchangeOrder.getInputAmount();
		double outputAmount = exchangeOrder.getOutputAmount();
		
		CurrencyData inputCurrencyData = new CurrencyData();
		inputCurrencyData = currencyDataLoader(exchangeOrder, "input");
		
		CurrencyData outputCurrencyData = new CurrencyData();
		outputCurrencyData = currencyDataLoader(exchangeOrder, "output");
		
		String result = calculateCurrencyExchangeResult(inputAmount, outputAmount, inputCurrencyData, outputCurrencyData);
		
		return result;
	}
	
	String currencyTypeToCurrencyCodeConverter(String currencyType) {
		
		switch (currencyType) {
		case "dolar amerykański":
			return "USD";
			
		case "dolar australijski":
			return "AUD";
			
		case "dolar kanadyjski":
			return "CAD";
			
		case "euro":
			return "EUR";
			
		case "forint":
			return "HUF";
			
		case "frank szwajcarski":
			return "CHF";
			
		case "funt szterling":
			return "GBP";
			
		case "jen":
			return "JPY";
			
		case "korona czeska":
			return "CZK";
			
		case "korona duńska":
			return "DKK";
			
		case "korona norweska":
			return "NOK";
			
		case "korona szwedzka":
			return "SEK";
			
		case "polski złoty":
			return "PLN";
			
		default:
			return "wybierz walutę";
		}
	}
	
	CurrencyData currencyDataLoader(ExchangeOrder exchangeOrder, String inputOrOutput) {
		CurrencyData currencyData = new CurrencyData();
		
		switch (inputOrOutput) {
		case "input":
			if (currencyTypeToCurrencyCodeConverter(exchangeOrder.getCurrencyInputType()).equals("PLN")) {
				currencyData = createCurrencyDataForPLN();
			} else {
				currencyData = currencyDataController.getCurrencyData(currencyTypeToCurrencyCodeConverter(exchangeOrder.getCurrencyInputType()));
			}
			break;
			
		case "output":
			if (currencyTypeToCurrencyCodeConverter(exchangeOrder.getCurrencyOutputType()).equals("PLN")) {
				currencyData = createCurrencyDataForPLN();
			} else {
				currencyData = currencyDataController.getCurrencyData(currencyTypeToCurrencyCodeConverter(exchangeOrder.getCurrencyOutputType()));
			}
			break;
		}
		
		return currencyData;
	}
	
	CurrencyData createCurrencyDataForPLN() {
		CurrencyData currencyData = new CurrencyData();
		currencyData.setCode("PLN");
		currencyData.setCurrency("polski złoty");
		
		Rate rate = new Rate();
		rate.setAsk(1.0);
		rate.setBid(1.0);
		ArrayList<Rate> ratesList = new ArrayList<>();
		ratesList.add(rate);
		currencyData.setRates(ratesList);
		return currencyData;
	}
	
	String calculateCurrencyExchangeResult(double inputAmount, double outputAmount,
			CurrencyData inputCurrencyData, CurrencyData outputCurrencyData) {
		
		double inputBid = inputCurrencyData.getRates().get(0).getBid();
		double outputAsk = outputCurrencyData.getRates().get(0).getAsk();
		
		if (inputAmount >= outputAmount) {
			outputAmount = (inputAmount * inputBid) / outputAsk;
			return "Za " + round(inputAmount) + " " + inputCurrencyData.getCode() + " otrzymasz " + round(outputAmount) + " " + outputCurrencyData.getCode() + ".";
			
		} else {
			inputAmount = (outputAmount * outputAsk) / inputBid;
			return "Za " + round(outputAmount) + " " + outputCurrencyData.getCode() + " zapłacisz " + round(inputAmount) + " " + inputCurrencyData.getCode() + ".";
		}
	}
	
	double round(double amount) {
		amount = (double)Math.round(amount * 100d) / 100d;
		
		return amount;
	}
}
