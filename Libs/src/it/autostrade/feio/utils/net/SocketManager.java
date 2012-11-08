package it.autostrade.feio.utils.net;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 03/feb/2012
 * Insieme di metodi utili per la gestione di socket e porta per la comunicazione di rete 
 */
public class SocketManager {
	
	/**
	 * @param port Porta da testare
	 * @return Vero se la porta è disponibile, falso altrimenti
	 */
	public static boolean isPortOpened(int port) {
		boolean opened;
		try {
			ServerSocket s = new ServerSocket(port);
			s.setReuseAddress(true);
			s.close();			
			opened = false;
		} catch (IOException e) {
			opened = true;
		}
		return opened;
	}
	
	
	public static boolean closePortForced(int port) {
		boolean closed;
		try {
			if (isPortOpened(port)) {
				ServerSocket s = new ServerSocket(port);
				s.setReuseAddress(true);
				s.close();			
			}	
			closed = true;
		} catch (IOException e) {
			closed = false;
		}
		return closed;		
	}
	
	
	
	public static void main(String[] args) {
		int port = 5000;
		try {
			ServerSocket s = new ServerSocket(port);
			s.setReuseAddress(true);
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(isPortOpened(port));
		System.out.println(closePortForced(port));
		System.out.println(isPortOpened(port));
	}
	
}
