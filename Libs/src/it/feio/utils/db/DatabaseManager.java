package it.feio.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Gestore accessi al Database
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 17/ott/2012
 *
 */
public class DatabaseManager {
	
	private String url, user, pass;
	private static String driver;
	static Connection conn = null;
	
	
	/* Metodi per la gestione della connessione */
	
	public DatabaseManager(String driver, String url, String user, String pass) {
		this.url = url;
		this.pass = pass;
		this.user = user;
		DatabaseManager.driver = driver;
	}
		
	
	public Connection getConnection(boolean autoCommit) throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, pass);
		conn.setAutoCommit(autoCommit);	
		return conn;
	}
	
	
	
	
	
	
	
}