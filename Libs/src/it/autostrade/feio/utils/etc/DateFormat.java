package it.autostrade.feio.utils.etc;

import java.util.*;
import java.text.*;

public class DateFormat {

	private Calendar cal = null;

	/**
	 * Classe per la gestione di date
	 * 
	 * @param dateString
	 *              Stringa contenente la data
	 * @param dateFormat
	 *              Formato della data passata come parametro
	 */
	public DateFormat(String dateString, String dateFormat) {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat(dateFormat);
			this.cal = Calendar.getInstance();
			this.cal.setTime((Date) formatter.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ritorna il giorno relativo l'oggetto DateFormat istanziato
	 * 
	 * @return Giorno del mese
	 */
	public long getDay() {
		return this.cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Ritorna il mese relativo l'oggetto DateFormat istanziato
	 * 
	 * @return Mese dell'anno
	 */
	public long getMonth() {
		return this.cal.get(Calendar.MONTH);
	}

	/**
	 * Ritorna l'anno relativo l'oggetto DateFormat istanziato
	 * 
	 * @return Anno
	 */
	public long getYear() {
		return this.cal.get(Calendar.YEAR);
	}

	/**
	 * Ritorna l'anno relativo la stringa passata come parametro
	 * 
	 * @param dateString
	 *              Stringa contenente la data
	 * @param dateFormat
	 *              Formato della data passata come parametro
	 * @return Anno
	 */
	public static int getYear(String dateString, String dateFormat) {
		return getCalendarObject(dateString, dateFormat).get(Calendar.YEAR);
	}

	/**
	 * Ritorna l'ora relativa la stringa passata come parametro
	 * 
	 * @param dateString
	 *              Stringa contenente la data
	 * @param dateFormat
	 *              Formato della data passata come parametro
	 * @return Ora
	 */
	public static int getHour(String dateString, String dateFormat) {
		return getCalendarObject(dateString, dateFormat).get(Calendar.HOUR);
	}

	private static Calendar getCalendarObject(String dateString, String dateFormat) {
		Date d = null;
		try {
			d = (Date) new SimpleDateFormat(dateFormat).parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}
}