package main.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class CurrencyData {

	private String table;
    private String currency;
    private String code;
    private ArrayList<Rate> rates;
}
