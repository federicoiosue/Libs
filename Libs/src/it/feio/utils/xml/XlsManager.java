package it.feio.utils.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 26/giu/2012 Utilità per la gestione di file Excel con POI >= 3.8
 */
public class XlsManager {

	private HSSFWorkbook wb;

	public XlsManager() {}

	public XlsManager(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public HSSFWorkbook getWorkbook() {
		return this.wb;
	}

	/**
	 * Costruttore che utilizza un file template
	 * 
	 * @param template
	 *              Url del file da usare come template
	 * @throws IOException
	 */
	public XlsManager(String template) throws IOException {
		FileInputStream fis = new FileInputStream(template);
		POIFSFileSystem fs = new POIFSFileSystem(fis);
		this.wb = new HSSFWorkbook(fs, true);
	}

	/**
	 * Converte tutte le celle del foglio che hanno un contenuto numerico in tipo numerico.
	 * 
	 * @param sheet
	 */
	public void parseNumeric(HSSFSheet sheet) {
		@SuppressWarnings("unchecked")
		Iterator<HSSFRow> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			HSSFRow row = rows.next();
			@SuppressWarnings("unchecked")
			Iterator<HSSFCell> cells = row.cellIterator();
			while (cells.hasNext()) {
				HSSFCell cell = cells.next();
				parseNumeric(cell);
			}
		}
	}

	/**
	 * Converte in tipo numerico il contenuto, se possibile.
	 * 
	 * @param cell
	 */
	public void parseNumeric(HSSFCell cell) {
		if (cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
			String val = cell.getRichStringCellValue().getString();
			try {
				Float num = Float.parseFloat(val);
				// System.out.println("Numero: " + num);
				cell.setCellValue(num);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
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
	public void rename(String sheetName, String sheetNewName) {
		this.wb.setSheetName(this.wb.getSheetIndex(sheetName), sheetNewName);
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
	 */
	public void commit(String outFile) throws IOException {
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
	 */
	public void commit(String outFile, String[] sheets) throws IOException {
		FileOutputStream fileOut;
		String outFileTmp = "";
		HSSFWorkbook wbOut = this.wb;
		
		// Nel caso debbano essere salvati solo alcuni fogli
		if (sheets != null) {
			
			// Viene creata una copia temporanea del workbook
			outFileTmp = "tmp_" + (int)Math.random()*1000;
			fileOut = new FileOutputStream(outFileTmp);
			this.wb.write(fileOut);
			fileOut.close();			
			XlsManager x = new XlsManager(outFileTmp);
			HSSFWorkbook tmpWb = x.getWorkbook();
			
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