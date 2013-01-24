package it.autostrade.feio.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import org.apache.commons.net.util.Base64;


/**
 * Gestisce la connessione, autenticazione e reperimento files tramite più protocolli da server remoti 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 15/feb/2012  
 */
public class HttpManager {


	/**
	 * Genera un oggetto di tipo proxy
	 * 
	 * @param ip
	 *              Indirizzo del proxy
	 * @param port
	 *              Porta sulla quale il proxy è in ascolto
	 * @return Oggetto proxy da usare per la connessione
	 */
	public static Proxy useProxy(String ip, int port) {
		// System.setProperty("http.proxyHost", url);
		// System.setProperty("http.proxyPort", port);
		return (new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
	}



	/**
	 * Genera la stringa per l'autenticazione base ad un server
	 * 
	 * @param usr
	 * @param pwd
	 * @return Stringa col valore della proprietà "Authorization" della connessione
	 */
	public static String basicAuthString(String usr, String pwd) {
		String authString = usr + ":" + pwd;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		return "Basic " + authStringEnc;
	}

	

	/**
	 * Recupera il contenuto di un URL
	 * 
	 * @param url
	 *              Url del contenuto da scaricare
	 * @return Stringa col contenuto della pagina
	 * @throws Exception 
	 */
	public static String fetchUrl(String url, String encoding) throws Exception {
		return fetchUrl(url, encoding, "", 0, "", "");
	}


	
	/**
	 * Recupera il contenuto di un URL tramite un proxy
	 * @param url Url del contenuto da scaricare
	 * @param proxy
	 * @param proxyPort
	 * @return Stringa col contenuto della pagina
	 * @throws Exception
	 */
	public static String fetchUrl(String url, String encoding, String proxy, int proxyPort) throws Exception {
		return fetchUrl(url, encoding, proxy, proxyPort, "", "");
	}



	/**
	 * Recupera il contenuto di un URL tramite un proxy ed autenticandosi con il server
	 * 
	 * @param url
	 *              Url del contenuto da scaricare
	 * @param proxy
	 *              Indirizzo IP del proxy
	 * @param usr
	 *              Nome utente per l'autenticazione lato server
	 * @param pwd
	 *              Password per l'autenticazione lato server
	 * @return Stringa col contenuto della pagina
	 * @throws Exception 
	 */
	public static String fetchUrl(String url, String encoding, String proxy, int proxyPort, String usr, String pwd) throws Exception {
		HttpURLConnection conn = null;
		String inputLine = null;
		StringBuilder pageUrl = new StringBuilder();
		BufferedReader in = null;
		try {
			if (proxy != "")
				conn = (HttpURLConnection) new URL(url).openConnection(useProxy(proxy, proxyPort));
			else
				conn = (HttpURLConnection) new URL(url).openConnection();
			if (usr != "" && pwd != "") {
				conn.setRequestProperty("Authorization", basicAuthString(usr, pwd));
			}
			conn.setRequestMethod("POST");
		} catch (IOException e) {
			throw (e);
		}
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
		} catch (Exception e) {
			throw(e);
		}
		while ((inputLine = in.readLine()) != null) {
			pageUrl.append(inputLine + System.getProperty("line.separator"));
		}
		in.close();
		return pageUrl.toString();
	}



	/**
	 * Verifica la raggiungibilità di un URL
	 * 
	 * @author 17000026
	 * @param URLName
	 *              Indirizzo http da verificare
	 * @return Vero se l'indirizzo specificato è raggiungibile, falso altrimenti
	 */
	public static boolean checkUrl(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * Verifica la raggiungibilità di un URL usando un proxy
	 * 
	 * @author 17000026
	 * @param URLName
	 *              Indirizzo http da verificare
	 * @param proxy
	 *              Indirizzo ip del proxy
	 * @param proxyPort
	 *              Porta del proxy
	 * @return
	 */
	public static boolean checkUrl(String URLName, String proxy, int proxyPort) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection(useProxy(proxy, proxyPort));
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
