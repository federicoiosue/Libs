package it.feio.utils.test;

import it.feio.utils.xls.XlsManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class TestPOIChart {


	public static void main(String[] args) {
		
		final String TEMPLATE = "./dat/template.xls";
		
		try {
			
			// Importa il template excel
			XlsManager xls = new XlsManager(TEMPLATE);
			Workbook wb = xls.getWorkbook();
			
			// Clona lo sheet vecchio e lo rinomina
			Sheet consecutivechart = wb.cloneSheet(1);
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
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}


	}



}
