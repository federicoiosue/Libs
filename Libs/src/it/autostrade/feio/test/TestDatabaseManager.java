package it.autostrade.feio.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import it.autostrade.feio.utils.DatabaseManager;
import it.autostrade.feio.utils.PropertiesReader;
import it.autostrade.feio.utils.SimpleXlsExporter;


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
		
		DatabaseManager dbM = new DatabaseManager(p.getProperty("db.driver"), p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.pass"));
		try {
			Connection conn = null;
			try {
				conn = dbM.getConnection(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			String sql = "SELECT" + 
						"	b_qry" + 
						" FROM" + 
						"	BMAA.tbma60_rep_eml" + 
						" WHERE" + 
						"	c_ute_mat     = '17000026'" + 
						"	AND d_ora_ric = '01-GEN-2012 12:30:00' FOR UPDATE";
//			String sql = p.getProperty("sql");
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			SimpleXlsExporter sxe = new SimpleXlsExporter();
			sxe.setDataHeader(rs);
			sxe.createXls(rs);
			System.out.println(sxe.getCsv());
//			File f = sxe.getFile("export.xls");
//			File f1 = new File("dat/exp.xls");
//			FileManager.move(f, f1);
//			System.out.println("Fatto");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
