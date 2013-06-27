package it.feio.utils.test;

import static org.junit.Assert.*;
import it.feio.utils.db.RsDump;
import it.feio.utils.file.PropertiesReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.junit.Test;


public class TestRsDump {

final static String configFilePath = "./conf/database.properties";
	
	public static void main(String[] args) {
		Properties p = null;
		try {
			p = PropertiesReader.parse(configFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		it.feio.utils.db.DatabaseManager dbM = new it.feio.utils.db.DatabaseManager(p.getProperty("db.driver"), p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.pass"));
		try {
			Connection conn = null;
			try {
//				String h = ProxyDetector.getInstance().getHostname();
				conn = dbM.getConnection(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			String sql = "SELECT * FROM BMTA.tbmt10_dat_met where rownum < 50";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery(); 
//			ArrayList<HashMap<String, String>> list = RsDump.dumpToMap(rs); 
			RsDump.dumpToconsole(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		
		
	}
	
	
	
}
