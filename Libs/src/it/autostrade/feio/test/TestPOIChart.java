package it.autostrade.feio.test;

import it.autostrade.feio.utils.xml.XlsManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestPOIChart {


	public static void main(String[] args) {
		
		try {
			
			// Importa il template excel
			XlsManager xls = new XlsManager("./dat/template.xls");
			HSSFWorkbook wb = xls.getWorkbook();
			
			// Clona lo sheet vecchio e lo rinomina
			HSSFSheet consecutivechart = wb.cloneSheet(1);
			xls.rename(consecutivechart, "nuovo");
			
			// Converte i numeri in tipo numerico
			xls.parseNumeric(consecutivechart);			
			
			// Salva il nuovo file
			xls.commit("./dat/workbook.xls");
			
			// Salva il nuovo file (solo alcuni sheets) 
			String[] sheetsToSave = {"incociate"};
			xls.commit("./dat/workbookSome.xls", sheetsToSave);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}



}
