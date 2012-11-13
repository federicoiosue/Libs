package it.autostrade.feio.utils.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe di tipo Hashmap specifica per essere usata come contatore
 * 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 17/apr/2012
 */
@SuppressWarnings("serial")
public class GenericMap extends HashMap<String, Object> {

	/**
	 * Incrementa il valore numerico associato ad una chiave
	 * 
	 * @param key
	 *              Chiave da incrementare
	 */
	public void increase(String key) {
		try {
			Integer val = Integer.parseInt(this.get(key).toString());
			this.put(key, ++val);
		} catch (NullPointerException e) {
			this.put(key, 1);
		} catch (NumberFormatException e) {}
	}


	/**
	 * Ritorna una lista di valori associati alle chiavi che soddisfano una espressione regolare
	 * 
	 * @param regex
	 *              Espressione regolare per cercare la chiave
	 * @return Lista dei valori trovati
	 */
	public ArrayList<Object> getRegex(String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m;
		ArrayList<Object> res = new ArrayList<Object>();
		Iterator<String> i = this.keySet().iterator();
		String key;
		while (i.hasNext()) {
			key = i.next();
			m = p.matcher(key);
			// System.out.println("Confronto " + regex + " con " + key);
			if (m.find()) {
				res.add(this.get(key));
			}
		}
		return res;
	}


	/**
	 * Ritorna la lista delle chiavi della mappa che soddisfano una espressione regolare
	 * 
	 * @param regex
	 *              Espressione regolare per cercare la chiave
	 * @return Lista dei valori trovati
	 */
	public ArrayList<String> getKeyRegex(String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m;
		ArrayList<String> res = new ArrayList<String>();
		Iterator<String> i = this.keySet().iterator();
		String key;
		while (i.hasNext()) {
			key = i.next();
			m = p.matcher(key);
			// System.out.println("Confronto " + regex + " con " + key);
			if (m.find()) {
				res.add(key);
			}
		}
		return res;
	}
	
	
	/**
	 * @param key Chiave da estrarre
	 * @return Valore intero del valore associaot alla chiave. Zero se non è un intero o la chiave non è presente.
	 */
	public int getInt(String key) {
		int val = 0;
		try {
			val = Integer.parseInt(this.get(key).toString());
		} catch (Exception e) {}
		return val;
	}
	
	

}