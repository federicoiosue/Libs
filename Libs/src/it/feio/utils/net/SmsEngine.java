package it.feio.utils.net;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * <p>Title: SMSEngine</p>
 * <p>Description: Invia un SMS utilizzando il server TAI</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/*
    Ciascuna invocazione del metodo statico "send", provoca la creazione di un thread separato, all'interno del costruttore
     che è stato opportunamente reso privato.

     Messaggi troppo lunghi vengono automaticamente spezzati in segmenti inviabili tramite sms.

*/

public class SmsEngine implements Runnable {
	
	private static int MAX_SMS_LENGHT = 150;
	private static final int TIMEOUT = 10000;
	private static URL sendURL;
	private static String user;
	private static String password;
	private static String domain;
	
	private static Logger log = Logger.getLogger("SMSEngine");

	private String address;
	
	private SmsEngine(String address) {
		this.address = address;
		Thread th = new Thread(this);
		th.start();
	}

	public void run() {
		try {
			HttpClient client = new HttpClient(sendURL);
			client.setTimeout(TIMEOUT);
			HttpResponse res = client.doGet(address);
			if(res.getStatusCode() != HttpResponse.ST_OK) {
				log.warn("Eccezione in invio SMS:" + res.getStatusCode() + res.getStatusMessage());
			}
		} catch(Exception ex) {
			// Vengono estratti dalla stringa address i valori dei campi recipient e text per stamparli nel log come numero di telefono e contenuto dell'sms
			String num = address.split("recipient=")[1].substring(0, (address.split("recipient=")[1].contains("&") ? address.split("recipient=")[1].indexOf("&") : address.split("recipient=")[1].length() ));
			String msg = address.split("text=")[1].substring(0, (address.split("text=")[1].contains("&") ? address.split("text=")[1].indexOf("&") : address.split("text=")[1].length() ));
			log.error("Eccezione in sendMessaggio (Tel. " + num + ", Msg. " + msg + "): " + ex.toString());
	    } 
	}
	
	public static boolean send(String number, String text) {
		int lunghezza_massima = MAX_SMS_LENGHT;
		if(sendURL == null ||
			user == null ||
			password == null ||
			domain == null) {
			log.error("Messaggio non inviato per configurazione incompleta");
			return false;
		}
		log.debug("Invio messaggio: " + text + " al numero: " + number);
		int pages = (int) (((double)text.length()) / MAX_SMS_LENGHT + 1);
		if (text.length() <= 160) {
			pages = 1;
			lunghezza_massima = 160;
		}
		if(pages > 1) {
			log.info("Il messaggio è suddiviso su " + pages + " pagine.");
		}
		for(int i = 0; i < pages; i ++) {
			try {
				String address = sendURL.toExternalForm() + // SMS FULL URL
								"?username=" + user + // USERNAME
								"&password=" + password + // USER PASSWORD
								"&domain=" + domain + // + // USER DOMAIN
								"&recipient=" + number + // TEL NUMBER
								"&text=" +URLEncoder.encode((pages > 1 ? ((i + 1) + "/" + pages + " ") : "") +
								text.substring(i * lunghezza_massima, Math.min((i + 1) * lunghezza_massima, text.length())), "UTF-8");
				new SmsEngine(address);
			} catch (UnsupportedEncodingException ex) {
				log.error("Eccezione in encoding:" + ex.toString());
				return false;
			}
		}
		return true;
	}

	public static void setDomain(String _domain) { 
		domain = _domain; 
	}
	
	public static void setPassword(String _password) { 
		password = _password; 
	}
	
	public static void setSendURL(URL _sendURL) { 
		sendURL = _sendURL; 
	}
	
	public static void setUser(String _user) { 
		user = _user; 
	}

}
