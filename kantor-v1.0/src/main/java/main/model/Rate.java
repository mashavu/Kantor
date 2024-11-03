package main.model;

import lombok.Data;

@Data
public class Rate {
	
	private String no;
	private String effectiveDate;
	private double bid;
	private double ask;
}
