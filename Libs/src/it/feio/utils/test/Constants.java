package it.feio.utils.test;

import java.math.BigDecimal;


public interface Constants {

	/* Generic constants */
	
	public static final String CONFIG_FILE_PATH = "./conf/database.properties";
	public static final String DATA_PATH = "./dat/";
	
	
	
	
	
	
	/* BMA */
	
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

	public static final String DATE_FORMAT_JAVA = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_SQL = "dd/mm/yyyy hh24:mi:ss";
	
	
	
}
