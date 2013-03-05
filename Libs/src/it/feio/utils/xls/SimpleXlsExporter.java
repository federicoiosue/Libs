package it.feio.utils.xls;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Font;
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
	private HSSFCellStyle sheetHeaderStyle;
	private HSSFCellStyle dataHeaderStyle;
	private Map<String, String> dataColumns;

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

	
	/**
	 * Imposta una riga di presentazione del documento [opizionale]
	 * @param text Testo della cella
	 * @param offset Offset in celle da sx
	 * @param span Larghezza in celle
	 */
	public void setSheetHeader(String text, Integer offset, Integer span) {
		List<Triplet<String, Integer, Integer>> list = new ArrayList<Triplet<String, Integer, Integer>>();
		list.add(new Triplet<String, Integer, Integer>(text, offset, span));
		this.sheetHeader = list;
	}
	

	
	/**
	 * Creazione eventuale riga di intestazione dello sheet
	 * @param report
	 */
	private void createSheetHeader(HSSFSheet report) {
		HSSFCell cell;
		if (this.sheetHeader != null) {
			HSSFRow header = report.createRow(verticalOffset++);
			for (Triplet<String, Integer, Integer> sheetHeaderElement : this.sheetHeader) {
				report.addMergedRegion(new CellRangeAddress(header.getRowNum(), header.getRowNum(), sheetHeaderElement.getValue1(), sheetHeaderElement.getValue1() + sheetHeaderElement.getValue2() - 1));
				cell = header.createCell(sheetHeaderElement.getValue1()); 
				cell.setCellValue(new HSSFRichTextString(sheetHeaderElement.getValue0()));
				if (sheetHeaderStyle != null)
					cell.setCellStyle(sheetHeaderStyle);
			}
		}		
	}


	
	public String[] getDataHeader() {
		return this.dataHeader;
	}

	
	
	/**
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale]
	 * Questa riga contiene i nomi delle colonne della tabella.
	 * @param header Mappa contenente il nome della colonna nel report ed il nome della colonna del ResultSet
	 * @throws SQLException 
	 */
	public void setDataHeader(Map<String, String> header) throws SQLException {
		// Salvataggio mappa campoResultSet-nomeColonna per usarlo alla creazione del report
		dataColumns = header;
		// Estrazione dei nomi delle colonne
		String dataHeader = "";		
		Iterator<?> it = header.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			dataHeader += "," + pair.getKey();
		}
		this.dataHeader = dataHeader.replaceFirst(",", "").split(",");
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
	 * Creazione eventuale riga di intestazione della tabella dati.
	 * @param report
	 */
	private void createDataHeader(HSSFSheet report) {
		HSSFCell cell;
		if (this.dataHeader != null) {
			HSSFRow header = report.createRow(verticalOffset++); 
			int cellCol = 0;
			for (String column : this.dataHeader) {
				cell = header.createCell(cellCol++);
				cell.setCellValue(new HSSFRichTextString(column));
				if (dataHeaderStyle != null)
					cell.setCellStyle(dataHeaderStyle);
			}
		}
	}
	
	
	
	/**
	 * Consente di personalizzare lo stile dellerighe di intestazione dello sheet
	 * @param styles Array contente i possibili valori: 'bold', 'italic', 'underline'
	 */
	public void setSheetHeaderStyle(String[] styles) {
		this.sheetHeaderStyle = setStyle(styles);
	}
	
	
	
	/**
	 * Consente di personalizzare lo stile dei nomi delle colonne della tabella dati
	 * @param styles Array contente i possibili valori: 'bold', 'italic', 'underline', 'center', 'right'
	 */
	public void setDataHeaderStyle(String[] styles) {
		dataHeaderStyle = setStyle(styles);
	}
	
	
	
	/**
	 * Personalizzazione stili testo
	 * @param styles
	 */
	private HSSFCellStyle setStyle(String[] styles) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		for (String style : styles) {
			if (style.toLowerCase().trim().equals("bold")) {
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			} else if (style.toLowerCase().trim().equals("italic")) {
				font.setItalic(true);
			} else if (style.toLowerCase().trim().equals("underline")) {
				font.setUnderline(HSSFFont.U_SINGLE);
			} else if (style.toLowerCase().trim().equals("center")) {
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			} else if (style.toLowerCase().trim().equals("right")) {
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			}  
			cellStyle.setFont(font);
		}
		return cellStyle;		
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

		// Creazione della lista delle colonne...
		List<String> fields = new ArrayList<String>();
		// ...se disponibile utilizzando l'header ed i nomi dei campi del ResultSet forniti
		if (dataColumns != null) {
			for (String header: dataHeader) {
				fields.add(dataColumns.get(header));
			} 
		} else {
			// ...altrimenti viene effettuato un recupero automatico dei campi del ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				fields.add(rsmd.getColumnName(i));
			}
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
	 * Blocca una riga o una colonna dello sheet
	 * @param colSplit
	 * @param rowSplit
	 */
	public void freezePane(int sheetNumber, int colSplit, int rowSplit){
		wb.getSheetAt(sheetNumber).createFreezePane(colSplit, rowSplit);
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
