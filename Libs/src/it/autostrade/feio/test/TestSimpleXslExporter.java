package it.autostrade.feio.test;

import it.autostrade.feio.utils.xls.SimpleXlsExporter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TestSimpleXslExporter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Persona> list = new ArrayList<Persona>();
//		list.add(new Persona("fede", "1"));
//		list.add(new Persona("lore", "2"));
//		list.add(new Persona("simo", "3"));

		SimpleXlsExporter xls = new SimpleXlsExporter();

		String dataHeader = "nome, cognome";
		try {
			xls.setDataHeader(dataHeader);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String[] methods = { "getNome", "getCognome" };
		try {
			xls.createXls(list, methods);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String csv = xls.getCsv();
		System.out.println(csv);
	}

}




