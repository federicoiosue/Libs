package it.feio.utils.net;

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
	
	
	/**
	 * Prova a chiudere una porta che al SO risulta utilizzata anche se il processo è stato terminato
	 * @param port
	 * @return
	 */
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
		
}
