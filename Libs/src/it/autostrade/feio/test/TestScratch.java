package it.autostrade.feio.test;

import it.autostrade.feio.utils.Dumper;
import java.awt.Color;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestScratch {


	public static void main(String[] args) {
		Persona f = new Persona("Federico", "Iosue", "via lallero", 31);
		System.out.println(Dumper.dump(new String("asd"), 0));
		
		String str = "federico";
//		Object value = Array.get(str.getClass().getDeclaredFields(), 0);
//		System.out.println(value);
//		System.out.println(str.getClass().equals(String.class));
		
	}

}

class Persona {
	public Persona(){}
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



interface ICostanti
{	
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

