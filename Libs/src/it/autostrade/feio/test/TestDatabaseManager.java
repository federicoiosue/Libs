package it.autostrade.feio.test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import it.autostrade.feio.utils.file.PropertiesReader;
import it.autostrade.feio.utils.file.FileManager;
import it.autostrade.feio.utils.xls.SimpleXlsExporter;


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
		
		it.autostrade.feio.utils.db.DatabaseManager dbM = new it.autostrade.feio.utils.db.DatabaseManager(p.getProperty("db.driver"), p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.pass"));
		try {
			Connection conn = null;
			try {
				conn = dbM.getConnection(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			String sql = "SELECT * FROM BMAA.tbma54_sto_alt_cl";
//			String sql = p.getProperty("sql");
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			SimpleXlsExporter sxe = new SimpleXlsExporter();
			
			HashMap<String, String> header = new HashMap<String, String>();
			header.put("utente", "C_UTE_MAT");
			header.put("ads", "C_ADS");
			header.put("data", "D_INI_ISP");
			sxe.setDataHeader(header);
			
			sxe.setDataHeaderStyle(new String[] {"bold"});
			sxe.createXls(rs);
			sxe.freezePane(0, 0, 1);
			
//			System.out.println(sxe.getCsv());
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
