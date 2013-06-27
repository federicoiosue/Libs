package it.feio.utils.test;

import it.feio.utils.file.PropertiesReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import org.hsqldb.lib.HashMap;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class TestJasperReports {

	public static String JASPER_REPORT_FOLDER = Constants.DATA_PATH;
	public static String JASPER_FILENAME = "report1";

	public static void main(String[] args) throws Exception {

		Properties p = null;
		try {
			p = PropertiesReader.parse(Constants.CONFIG_FILE_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// inizializzazione connessione al database
		it.feio.utils.db.DatabaseManager dbM = new it.feio.utils.db.DatabaseManager(p.getProperty("db.driver"), p.getProperty("db.url"), p.getProperty("db.user"), p.getProperty("db.pass"));
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = dbM.getConnection(true);

			PreparedStatement ps = conn.prepareStatement("SELECT" + "	t_des_tip_rep," + "	n_prg_tip_rep," + "	COUNT(n_prg_tip_rep)" + " FROM" + "	bmaa.tbma60_rep_eml" + " JOIN bmaa.tbma46_tip_rep USING(n_prg_tip_rep)" + " GROUP BY" + "	t_des_tip_rep," + "	n_prg_tip_rep");
			rs = ps.executeQuery();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


		// caricamento file JRXML
		JasperDesign jasperDesign = JRXmlLoader.load(JASPER_REPORT_FOLDER + JASPER_FILENAME + ".jrxml");
		// compilazione del file e generazione del file JASPER
		JasperCompileManager.compileReportToFile(jasperDesign, JASPER_REPORT_FOLDER + JASPER_FILENAME + ".jasper");



		// rendering e generazione del file PDF
		// JasperPrint jp = JasperFillManager.fillReport(JASPER_REPORT_FOLDER + JASPER_FILENAME + ".jasper", null, conn);
		Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("nome", "PUPPA");
		JasperPrint jp = JasperFillManager.fillReport(JASPER_REPORT_FOLDER + JASPER_FILENAME + ".jasper", map, new JRResultSetDataSource(rs));
		JasperExportManager.exportReportToPdfFile(jp, JASPER_REPORT_FOLDER + "report.pdf");



	}
}