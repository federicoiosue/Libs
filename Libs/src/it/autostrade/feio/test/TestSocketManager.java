package it.autostrade.feio.test;

import it.autostrade.feio.utils.net.SocketManager;
import java.io.IOException;
import java.net.ServerSocket;


public class TestSocketManager {

	
	
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
		System.out.println(SocketManager.isPortOpened(port));
		System.out.println(SocketManager.closePortForced(port));
		System.out.println(SocketManager.isPortOpened(port));
	}
}
