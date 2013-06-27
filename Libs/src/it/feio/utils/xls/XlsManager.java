package it.feio.utils.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 26/giu/2012 Utilità per la gestione di file Excel con POI >= 3.9 * 
 */
public class XlsManager {

	private Workbook wb;

	public XlsManager() {}

	public XlsManager(XSSFWorkbook wb) {
		this.wb = wb;
	}

	public Workbook getWorkbook() {
		return this.wb;
	}

	
	/**
	 * Costruttore che utilizza un file template
	 * 
	 * @param template
	 *              Url del file da usare come template
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public XlsManager(String template) throws IOException, InvalidFormatException {
//		this.wb = new XSSFWorkbook(new FileInputStream(template));
		this.wb = WorkbookFactory.create(new File(template));
	}

	
	/**
	 * Converte tutte le celle del foglio che hanno un contenuto numerico in tipo numerico.
	 * 
	 * @param sheet
	 */
	public void parseNumeric(Sheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				parseNumeric(cell);
			}
		}
	}

	
	/**
	 * Converte in tipo numerico il contenuto, se possibile.
	 * 
	 * @param cell
	 */
	public void parseNumeric(Cell cell) {
		if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
			String val = cell.getRichStringCellValue().getString();
			try {
				Float num = Float.parseFloat(val);
				// System.out.println("Numero: " + num);
				cell.setCellValue(num);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			} catch (NumberFormatException e) {
				// System.out.println("Stringa: " + val);
			}
		}
	}

	
	/**
	 * Cambia nome al foglio
	 * 
	 * @param sheetName
	 *              Nome del foglio excel
	 * @param sheetNewName
	 *              Nuove nome del foglio
	 */
	public void rename(Sheet sheet, String newName) {
		String sheetName = sheet.getSheetName();
		this.wb.setSheetName(this.wb.getSheetIndex(sheetName), newName);
	}

	
	/**
	 * Cambia nome al foglio
	 * 
	 * @param sheet
	 *              Nome del foglio excel
	 * @param sheetNewName
	 *              Nuove nome del foglio
	 */
	public void rename(HSSFSheet sheet, String sheetNewName) {
		this.wb.setSheetName(this.wb.getSheetIndex(sheet), sheetNewName);
	}

	
	/**
	 * Salva la cartella excel su un file
	 * 
	 * @param outFile
	 *              Nome dle file
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public void commit(String outFile) throws IOException, InvalidFormatException {
		commit(outFile, null);
	}

	
	/**
	 * Salva determinati fogli della cartella excel su un file
	 * 
	 * @param outFile
	 *              Nome dle file
	 * @param sheets
	 *              Nomi dei fogli
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public void commit(String outFile, String[] sheets) throws IOException, InvalidFormatException {
		FileOutputStream fileOut;
		String outFileTmp = "";
		Workbook wbOut = this.wb;
		
		// Nel caso debbano essere salvati solo alcuni fogli
		if (sheets != null) {
			
			// Viene creata una copia temporanea del workbook
			outFileTmp = "tmp_" + (int)Math.random()*1000;
			fileOut = new FileOutputStream(outFileTmp);
			this.wb.write(fileOut);
			fileOut.close();			
			XlsManager x = new XlsManager(outFileTmp);
			Workbook tmpWb = x.getWorkbook();
			
			// Controlla quali fogli dell cartella excel cancellare dalla copia temporanea
			boolean found;
			for(int i=0; i<tmpWb.getNumberOfSheets(); i++){			
				found = false;
				for (String sheet : sheets) {
					if (tmpWb.getSheetName(i) == sheet){
						found = true;
					}
				}
				// TODO NON CANCELLA "INCROCIATE"
				if (!found)
					tmpWb.removeSheetAt(i);
			}
							
			wbOut = tmpWb;
		}
		
		// Salva il file
		fileOut = new FileOutputStream(outFile);
		wbOut.write(fileOut);
		fileOut.close();
		
		// Cancellazione dle file temporaneo
		if (sheets != null) {
			File f = new File(outFileTmp);
			f.delete();
		}
	}

}