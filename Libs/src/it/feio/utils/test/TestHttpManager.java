package it.feio.utils.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import it.feio.utils.file.FileManager;
import it.feio.utils.net.HttpManager;



public class TestHttpManager {

	public static void main(String[] args) {
		
		String proxy = "201.91.28.19";
		int proxyPort = 8080;
		String usr = "tronco4";
		String pwd = "Auto2009";

		String url = "http://centrometeoitaliano.it/test/Siti_Autostrade_00_23012013.xml";
//		String url = "http://www.smr.arpa.emr.it/autostrade/downloadXml.php";
//		String url = "http://www.techpowerup.com/forums/showthread.php?t=96054";
//		String url = "http://www.oracle.com/it/index.html";

//		System.out.println(HttpManager.checkUrl(url, proxy, port));
		
//		HttpManager.setProxy("201.91.28.19", "8080");
		
		String file = null;
		try {
//			file = HttpManager.fetchUrl(url, proxy, proxyPort, usr, pwd);	
//			file = HttpManager.fetchUrl(url, "UTF-8", proxy, proxyPort);			
			Date d = HttpManager.getLastModified(url, proxy, proxyPort);			
//			file = HttpManager.fetchUrl(url);			
			
//			FileManager fm = new FileManager();
			try {
				FileManager.write(file, "provaDwnlXml.xml");
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		

	}



}
