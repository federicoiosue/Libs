package it.autostrade.feio.test;

import it.autostrade.feio.utils.DateFormat;

public class TestDateFormat {

	public static void main(String[] args) {
		try {
			String dateString = "2010-05-12 03:34:00.0";
			String dateFormat = "yyyy-MM-dd HH:mm:ss.S";
			
			DateFormat date = new DateFormat(dateString, dateFormat);
			System.out.println(date.getYear());			
			
			System.out.println(DateFormat.getYear(dateString, dateFormat));
			System.out.println(DateFormat.getHour(dateString, dateFormat));
			
			
			
		} catch (Exception e) {
			System.out.println("Exception :" + e);
		}
		
	}
}