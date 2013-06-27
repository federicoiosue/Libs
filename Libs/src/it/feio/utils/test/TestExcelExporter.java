package it.feio.utils.test;

import it.feio.utils.file.PropertiesReader;
import it.feio.utils.xls.ExcelExporter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;


public class TestExcelExporter {

	final static String configFilePath = "./conf/database.properties";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		Properties p = null;
		try {
			p = PropertiesReader.parse(configFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		it.feio.utils.db.DatabaseManager dbM = new it.feio.utils.db.DatabaseManager(p.getProperty("db.driver"), p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.pass"));

		try {

			Connection conn = null;
			try {
				conn = dbM.getConnection(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			String sql = "SELECT" + "	c_ads," + "	c_att," + "	c_kpi," + "	d_ini_isp," + "	'https://www1.gruppo.autostrade.it/BMA/CaricaFoto?cads='" + "	||c_ads" + "	||'&'||'diniisp='" + "	||TO_CHAR(d_ini_isp, 'yyyymmddhh24miss')" + "	||'&'||'catt='" + "	||c_att" + "	||'&'||'ckpi='" + "	||c_kpi url" + " FROM" + "	BMAA.tbma19_fto_cat";
//			String sql = "SELECT '1.2' AS asd, '1.5' as LOL, '1.6' as CRISHTO, '3.5' as FAAA, '2.7' as MOOOO FROM dual";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			ExcelExporter xls = new ExcelExporter();
			xls.setDefaultLinkString("Vedi");
			
			// Column types settings
			TreeMap<String, Integer> types = new TreeMap<String, Integer>();
			types.put("C_ATT", xls.CELL_TYPE_NUMERIC);
			types.put("URL", xls.CELL_TYPE_URL);
			xls.setColumnsTypes(types);

			try {
				xls.createXls(rs);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			xls.write("dat/file.xlsx");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
