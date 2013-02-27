package it.autostrade.feio.utils.net;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Classifica basandosi sui dati contenuti nell'header di una richiesta al server il tipo di browser 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 22/feb/2013
 *
 */
public class HeaderInfo {
	
	// Regex per la ricerca del tipo di browser
	private final static String[] PATTERNS = {
												"MSIE\\s*[0-9]+\\.[0-9]+",
												"Chrome/([0-9]+\\.)+[0-9]",
												"Firefox/([0-9]+\\.)+[0-9].*",
												"Opera/([0-9]+\\.)+[0-9]",
												"Safari/([0-9]+\\.)+[0-9]",
												"Skyfire"
												};
	
	 // Regex per la i browser di tipo mobile
	private final static String[] PATTERNS_MOBILE = {
												"Mobile",
												"Android",
												"Maemo",
												"S60",
												"bada",
												"fennec",
												"phone",
												"Skyfire"
												};
	
	
	/**
	 * Ritorna una mappa dei parametri dell'header di una richiesta HttpServletRequest
	 * @param request
	 * @return Mappa dei valori
	 */
	public static Map<String, String> getMap(HttpServletRequest request) {
		Map<String, String> headerMap = new HashMap<String, String>();
		for (Enumeration<?> headers = request.getHeaderNames(); headers.hasMoreElements();) {
			    String headerName = (String) headers.nextElement();
			    headerMap.put(headerName, request.getHeader(headerName));
			}
		return headerMap;
	} 
	
	
	/**
	 * Returns an object describing browser, if identified, and platform
	 * @param headerMap 
	 * @return Browser data map formes as {name, version, platform} 
	 */
	public static Map<String, String> getBrowser(Map<String, String> headerMap) {
		
		// Inizializzazione
		Map<String, String> browser = new HashMap<String, String>();
		String userAgent = headerMap.get("User-Agent");
		Pattern p;
		Matcher m;
		
		// Ricerca delle regex per l'identificazione del browser
		browser.put("name", "");
		browser.put("version", "");
		for (String pattern : PATTERNS) {
			p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			m = p.matcher(userAgent);
			if (m.find()) {
				browser.put("name", m.group().split("\\s|/")[0]);
				browser.put("version", m.group().split("\\s|/")[1]);
				break;
			}		
		}
		
		// Ricerca delle regex per l'identificazione del client come desktop o mobile
		browser.put("platform", "Desktop");
		for (String pattern : PATTERNS_MOBILE) {
			p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			m = p.matcher(userAgent);
			if (m.find()) {
				browser.put("platform", "Mobile");
				break;
			}		
		}
		
		return browser;
	}
	
	
	/**
	 * Ritorna il browser identificato in formato stringa accodando nome, versione e piattaforma
	 * @param headerMap
	 * @return 
	 */
	public static String getBrowserToString(Map<String, String> headerMap) {
		Map<String, String> browser = getBrowser(headerMap);
		return browser.get("name") + " " + browser.get("version") + " " + browser.get("platform");
	}
	
	
	/**
	 * Ritorna l'IP 
	 * @param headerMap
	 * @return
	 */
	public static String getIp(Map<String, String> headerMap) {
		String ipAdress = "";
		if (headerMap.get("HTTP_X_FORWARDED_FOR") != null) {
			ipAdress = headerMap.get("HTTP_X_FORWARDED_FOR");
		} else if (headerMap.get("Remote_Addr") != null) {
			ipAdress = headerMap.get("Remote_Addr");
		} else {
			ipAdress = headerMap.get("Remote_Addr");
		}
		return ipAdress;
	}
}
