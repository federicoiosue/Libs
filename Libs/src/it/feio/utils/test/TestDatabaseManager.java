package it.feio.utils.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import com.itextpdf.text.log.SysoLogger;
import it.feio.utils.db.NamedParameterStatement;
import it.feio.utils.file.FileManager;
import it.feio.utils.file.PropertiesReader;
import it.feio.utils.net.ProxyDetector;
import it.feio.utils.xls.SimpleXlsExporter;


public class TestDatabaseManager {

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
			
			String sql = "SELECT * FROM BMTA.tbmt10_dat_met where rownum < :rownum";
//			String sql = p.getProperty("sql");
			
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
			it.feio.utils.db.NamedParameterStatement nps = new NamedParameterStatement(conn, sql);
			nps.setInt("rownum", 100);
			ResultSet rs = nps.executeQuery();
			
			SimpleXlsExporter sxe = new SimpleXlsExporter();
			
			HashMap<String, String> header = new HashMap<String, String>();
			sxe.setDataHeader(rs);
			sxe.createXls(rs);
			sxe.freezePane(0, 0, 1);
			
			File f = sxe.getFile("export.xls");
			File f1 = new File("dat/exp.xls");
			FileManager.move(f, f1);
			
			System.out.println("Fatto");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
