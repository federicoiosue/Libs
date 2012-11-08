package it.autostrade.feio.utils;

import java.util.HashMap;

/**
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 24/set/2012
 *	Genera casualmente dei colori
 */
public class ColorFactory {

	HashMap<String, String> usedColors = new HashMap<String, String>();

	private ColorFactory() {}

	private static class Contenitore {

		private final static ColorFactory ISTANZA = new ColorFactory();
	}

	public static ColorFactory getInstance() {
		return Contenitore.ISTANZA;
	}

	/**
	 * Ritorna 
	 * @return
	 */
	public String getColor() {
		return getColor("");
	}

	public String getColor(String token) {
		String color = null;
		if (!token.isEmpty() && usedColors.containsKey(token)) {
			color = usedColors.get(token);
		} else {
			Double c = Math.floor(Math.random() * 0xFFFFFF);
			Integer i = c.intValue();
			color = new String(Integer.toHexString(i));
			if (!token.isEmpty()) {
				usedColors.put(token, color);
			}			
		}
		return color;
	}


}