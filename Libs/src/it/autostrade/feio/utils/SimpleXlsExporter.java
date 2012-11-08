package it.autostrade.feio.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.javatuples.Triplet;


/**
 * Classe di utilità per l'esportazione in formato Excel di una lista di oggetti complessi.
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 01/ago/2012
 *
 */
public class SimpleXlsExporter {

	HSSFWorkbook wb = new HSSFWorkbook();
	private int verticalOffset = 0;
	private List<Triplet<String, Integer, Integer>> sheetHeader;
	private String[] dataHeader;

	public int getVerticalOffset() {
		return verticalOffset;
	}

	
	/**
	 * Imposta un offset verticale in celle da lasciare vuote dall'inizio del documento.
	 * @param verticalOffset
	 */
	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public List<Triplet<String, Integer, Integer>> getSheetHeader() {
		return sheetHeader;
	}

	/**
	 * Imposta una riga di presentazione del documento [opizionale]
	 * @param sheetHeader Tripletta contenente: testo, offset (in celle da sx), larghezza (in celle)
	 */
	public void setSheetHeader(List<Triplet<String, Integer, Integer>> sheetHeader) {
		this.sheetHeader = sheetHeader;
	}


	public String[] getDataHeader() {
		return this.dataHeader;
	}

	
	/**
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale]
	 * Questa riga contiene i nomi delle colonne della tabella.
	 * @param rs Resultset dal quale estrarre automaticamente i nomi delle colonne
	 * @throws SQLException 
	 */
	public void setDataHeader(ResultSet rs) throws SQLException {		
		// Estrapolazione dei metadati dei risultati per i nomi delle colonne della tabella
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		// Nomi delle colonne
		String dataHeader = "";
		for (int i = 1; i < numColumns + 1; i++) {
			dataHeader += "," + rsmd.getColumnName(i);
		}
		this.dataHeader = dataHeader.replaceFirst(",", "").split(",");
	}
	
	
	/**
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale]
	 * Questa riga contiene i nomi delle colonne della tabella.
	 * @param dataHeader Stringa contenente i nomi delle colonne divisi da virgola
	 * @throws SQLException
	 */
	public void setDataHeader(String dataHeader) throws SQLException {
		this.dataHeader = dataHeader.split(",");
	}


	/**
	 * Crea l'oggetto rappresentante il file excel a partire da una lista generica di oggetti dotati di metodi getters per ottenere i dati
	 * @param list Lista di oggetti rappresentanti le righe del file excel
	 * @param methods Array con i nomi dei metodi da richiamare per ottenere il dato della singola cella
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void createXls(List<?> list, String[] methods) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		HSSFSheet report = wb.createSheet();
		
		createSheetHeader(report);
		createDataHeader(report);

		// Stampa del contenuto
		int rowNum = 1;
		int cellNum = 0;
		HSSFRow row;
		Method method = null;
		String cellContent = "";

		// Cicla gli oggetti della lista per stamparne uno per riga
		for (Object object : list) {
			row = report.createRow(rowNum++);
			cellNum = 0;

			// Cicla l'array con i nomi dei metodi e tramite reflection stampa in ogni cella della riga il dato opportuno
			for (String methodName : methods) {
				method = object.getClass().getMethod(methodName, new Class<?>[] {});
				cellContent = method.invoke(object, new Object[] {}).toString();
				// I numeri vengono tipizzati come tali nel foglio excel, se possibile
				try {
					row.createCell(cellNum).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					row.createCell(cellNum).setCellValue(Float.parseFloat(cellContent));
				} catch (NumberFormatException e) {
					row.createCell(cellNum).setCellValue(new HSSFRichTextString(cellContent));
				} catch (NullPointerException e) {
					row.createCell(cellNum).setCellValue(new HSSFRichTextString(""));
				}
				cellNum++;
			}

			// Gestione file troppo grande
			if (rowNum + 2 >= 65536) {
				report.getRow(0).getCell(report.createRow(0).getLastCellNum() + 1).setCellValue(new HSSFRichTextString("ATTENZIONE: superato numero massimo righe del foglio Excel!!"));
				break;
			}
		}
	}


	/**
	 * Creazione eventuale riga di intestazione dello sheet
	 * @param report
	 */
	private void createSheetHeader(HSSFSheet report) {
		if (this.sheetHeader != null) {
			HSSFRow header = report.createRow(verticalOffset++);
			for (Triplet<String, Integer, Integer> sheetHeaderElement : this.sheetHeader) {
				report.addMergedRegion(new CellRangeAddress(header.getRowNum(), header.getRowNum(), sheetHeaderElement.getValue1(), sheetHeaderElement.getValue1() + sheetHeaderElement.getValue2() - 1));
				header.createCell(sheetHeaderElement.getValue1()).setCellValue(new HSSFRichTextString(sheetHeaderElement.getValue0()));
			}
		}		
	}
	
	


	/**
	 * Creazione eventuale riga di intestazione della tabella dati.
	 * @param report
	 */
	private void createDataHeader(HSSFSheet report) {
		if (this.dataHeader != null) {
			HSSFRow header = report.createRow(verticalOffset++); 
			int cellCol = 0;
			for (String column : this.dataHeader) {
				header.createCell(cellCol++).setCellValue(new HSSFRichTextString(column));
			}
		}
	}


	
	/**
	 * Crea l'oggetto rappresentante il file excel a partire da un ResultSet definendone automaticamente le colonne sulla base dei campi risultanti dalla query.
	 * @param list Lista di oggetti rappresentanti le righe del file excel
	 * @param methods Array con i nomi dei metodi da richiamare per ottenere il dato della singola cella
	 * @throws SQLException 
	 */
	public void createXls(ResultSet rs) throws SQLException  {

		HSSFSheet report = wb.createSheet();

		createSheetHeader(report);
		createDataHeader(report);

		// Stampa del contenuto
		int rowNum = verticalOffset;
		int cellNum = 0;
		HSSFRow row;
		String cellContent = "";

		// Nomi dei campi del ResultSet
		ResultSetMetaData rsmd = rs.getMetaData();
		List<String> fields = new ArrayList<String>();
		for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
			fields.add(rsmd.getColumnName(i));
		}

		// Cicla gli oggetti della lista per stamparne uno per riga
		while (rs.next()) {
			row = report.createRow(rowNum++);
			cellNum = 0;			
			
			// Cicla l'array con i nomi dei metodi e tramite reflection stampa in ogni cella della riga il dato opportuno controllando anche se si tratta di un numero
			for (String field : fields) {				
				cellContent = rs.getString(field);
				try {
					row.createCell(cellNum).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					row.createCell(cellNum).setCellValue(Float.parseFloat(cellContent));
				} catch (NumberFormatException e) {
					row.createCell(cellNum).setCellValue(new HSSFRichTextString(cellContent));
				} catch (NullPointerException e) {
					row.createCell(cellNum).setCellValue(new HSSFRichTextString(""));
				}
				cellNum++;
			}

			// Gestione file troppo grande
			if (rowNum + 2 >= 65536) {
				report.getRow(0).getCell(report.createRow(0).getLastCellNum() + 1).setCellValue(new HSSFRichTextString("ATTENZIONE: superato numero massimo righe del foglio Excel!!"));
				break;
			}
		}
	}
	
	

	
	/**
	 * Ottiene la versione csv dell'excel
	 * @return Stringa col contenuto del file csv
	 */
	public String getCsv() {
		StringBuilder sb = new StringBuilder();
		HSSFSheet sheet = wb.getSheetAt(0);

		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);

			for (int j = 0; j <= row.getLastCellNum(); j++) {
				HSSFCell cell = row.getCell(j);
				try {
					try {
						sb.append("\"" + cell.getNumericCellValue() + "\",");
					} catch (IllegalStateException e) {
						sb.append("\"" + cell.getRichStringCellValue() + "\",");
					}					
				} catch (NullPointerException e) {}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";\n");
		}
		return sb.toString();
	}
	
	

	/**
	 * Crea un file temporaneo col report e ne ritorna un handler. 
	 * Viena automaticamente impostato per la cancellazione alla fine dell'esecuzione (se usato da Tomcat cancellare a mano).
	 * @return Handler del file temporaneo
	 * @throws IOException
	 */
	public File getFile() throws IOException {
		return getFile(null);
	}
	
	
	/**
	 * Crea un file temporaneo col report e ne ritorna un handler. 
	 * Viena automaticamente impostato per la cancellazione alla fine dell'esecuzione (se usato da Tomcat cancellare a mano).
	 * @param fileName Nome del file
	 * @return
	 * @throws IOException
	 */
	public File getFile(String fileName) throws IOException {
		if (fileName == null)
			fileName = "export_";				
		File f = File.createTempFile(fileName, ".xls");
		f.deleteOnExit();
		FileOutputStream fos = new FileOutputStream(f);
		wb.write(fos);
		fos.close();
		return f;
	}

	
	/**
	 * Invia al browser i dati per lo scaricamento del file
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	public void download(HttpServletResponse response, String fileName) throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + ".xls\"");
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}

}
