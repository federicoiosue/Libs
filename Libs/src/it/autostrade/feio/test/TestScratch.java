package it.autostrade.feio.test;

import it.autostrade.feio.utils.obj.MapSplitter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestScratch {


	public static void main(String[] args) {


		String s = "SALES:0,SALE_PRODUCTS:1,EXPENSES:2,EXPENSES_ITEMS:3";
		Map<String, Integer> lMap = new HashMap<String, Integer>();

		System.out.println(MapSplitter.on(",").withKeyValueSeparator(":").split(s));
		
	}

}




class Persona {

	public Persona() {}

	public Persona(String name, String surname, String address, int age) {
		super();
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.age = age;
	}

	String name;
	String surname;
	String address;
	int age;
}



interface ICostanti {

	public static final BigDecimal REPORT_PreAlert = BigDecimal.ONE;
	public static final BigDecimal REPORT_Consuntivo = BigDecimal.valueOf(2);
	public static final BigDecimal REPORT_Sintesi = BigDecimal.valueOf(3);
	public static final BigDecimal REPORT_SchedaDiRilevazione = BigDecimal.valueOf(4);
	public static final BigDecimal REPORT_CDA = BigDecimal.valueOf(5);
	public static final BigDecimal REPORT_Calendario = BigDecimal.valueOf(6);
	public static final BigDecimal REPORT_Qualità = BigDecimal.valueOf(7);
	public static final BigDecimal REPORT_Valutazione = BigDecimal.valueOf(8);
	public static final BigDecimal REPORT_Penali = BigDecimal.valueOf(9);

	// Per alcune funzioni asincrone è necessario avere nel database argomenti e/o oggetti da passare a dei metodi
	// (es. Report Email che usa allo scopo il campo BMAA.tbma60_rep_eml.t_uti)
	public static final String SEPARATORE_OGGETTI = "!!!";
	public static final String SEPARATORE_ARGOMENTI = ";;";

	public static final String FORMATO_DATA = "yyyyMMdd_HHmm";
}
