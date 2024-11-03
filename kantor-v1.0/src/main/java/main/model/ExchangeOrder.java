package main.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ExchangeOrder {
	
	@Pattern(regexp = "^(?!.*wybierz walutę).*$")
	private String currencyInputType;
	@Pattern(regexp = "^(?!.*wybierz walutę).*$")
	private String currencyOutputType;
	
	private double inputAmount;
	private double outputAmount;
}
