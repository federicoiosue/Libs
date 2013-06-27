package it.feio.utils.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import it.feio.utils.file.PropertiesReader;
import it.feio.utils.pdf.PdfExporter;
import it.feio.utils.pdf.Style;


public class TestPdfExporter {

	final static String configFilePath = "./conf/database.properties";
	
	public static void main(String[] args) {
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
			
//			String sql = "SELECT * FROM BMAA.tbma10_ril_cer WHERE rownum<1000";
			String sql = 	"SELECT" + 
					"	c_ads," + 
					"	c_att," + 
					"	c_kpi," + 
					"	d_ini_isp," + 
					"	'https://www1.gruppo.autostrade.it/BMA/CaricaFoto?cads='" + 
					"	||c_ads" + 
					"	||'&'||'diniisp='" + 
					"	||TO_CHAR(d_ini_isp, 'yyyymmddhh24miss')" + 
					"	||'&'||'catt='" + 
					"	||c_att" + 
					"	||'&'||'ckpi='" + 
					"	||c_kpi url" + 
					" FROM" + 
					"	BMAA.tbma19_fto_cat";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			PdfExporter spe = new PdfExporter(p.getProperty("datPath") + "export" + Calendar.getInstance().getTimeInMillis() + ".pdf");
			spe.setDefaultLinkString("Link");
			spe.setPageMargins(30, 30, 110, 150);
			
			String pageHeader = "Prova intestazione pagina";
			spe.setPageHeader(pageHeader);
			
//			Map<String, String> h = new HashMap<String, String>();
//			h.put("http://www.geek-tutorials.com", "c_ads");
//			H.PUT("KIPPO", "C_KPI");
//			H.PUT("VALEUR", "C_VAL");
//			spe.setDataHeader(h);
			
			// Stile intestazione tabella
			Style style = new Style();
			style.setFont(FontFactory.HELVETICA).setSize(6).setBackground("#C0C0C0").setBorders("", 0, 0, 1f, 1f);
			spe.setPageHeaderStyle(style);
			
			Style style2 = new Style();
			style2.setFont(FontFactory.HELVETICA).setSize(6).setBackground("#E5E5E5").setBorders("", 0.5f, 0.5f, 0.5f, 0.5f);
			spe.setDataHeaderStyle(style2);
			
			Style style1 = new Style();
			style1.setFont(FontFactory.HELVETICA).setSize(6).setBackground("#FFFFFF").setBorders("", 0, 0, 0, 0.5f);
			spe.setDataStyle(style1);
			
			spe.build(rs);
						
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fatto");
		}
	}
	
	
	

}
