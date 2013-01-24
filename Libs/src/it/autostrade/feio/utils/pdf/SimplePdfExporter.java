package it.autostrade.feio.utils.pdf;

import it.autostrade.feio.utils.obj.MemoryManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font;

public class SimplePdfExporter {

	private String pageHeader;
	private String[] dataHeader;
	private Map<String, String> dataColumns;
	private int[] dataHeaderWidths;
	private File tmpFile;

	
	/**
	 * Imposta un'intestazione testuale per ogni pagina del documento
	 * @param pageHeader Stringa con l'intestazione
	 */
	public void setPageHeader(String pageHeader) {
		this.pageHeader = pageHeader;		
	}


	/**
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale] Questa riga contiene i nomi delle colonne della tabella.
	 * 
	 * @param header
	 *              Mappa contenente il nome della colonna nel report ed il nome della colonna del ResultSet
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
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale] Questa riga contiene i nomi delle colonne della tabella.
	 * 
	 * @param rs
	 *              Resultset dal quale estrarre automaticamente i nomi delle colonne
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
	 * Imposta un'intestazione per la tabella dei dati del file esportato [opzionale] Questa riga contiene i nomi delle colonne della tabella.
	 * 
	 * @param dataHeader
	 *              Stringa contenente i nomi delle colonne divisi da virgola
	 * @throws SQLException
	 */
	public void setDataHeader(String dataHeader) throws SQLException {
		this.dataHeader = dataHeader.split(",");
	}

	public void setDataHeaderWidths(int[] dataHeaderWidths) {
		this.dataHeaderWidths = dataHeaderWidths;
	}



	private void buildPageHeader(PdfPTable table) {
		if (pageHeader != null) {
			PdfPCell c = createCell(pageHeader, FontFactory.HELVETICA, 7, 1, Element.ALIGN_LEFT, BaseColor.BLACK, BaseColor.GRAY);
			// c.setGrayFill(0.9f);
			c.setColspan(dataHeader.length);
			table.addCell(c);
			table.setHeaderRows(table.getHeaderRows()+1);
		}
	}
	
	
	
	private void buildDataHeader(PdfPTable table) {
		if (dataHeader != null) {
			PdfPCell c;
			for (String header : dataHeader) {
				c = createCell(header, FontFactory.HELVETICA, 7, 1, Element.ALIGN_CENTER, BaseColor.BLACK, BaseColor.LIGHT_GRAY);
				// c.setGrayFill(0.9f);
				table.addCell(c);
			}
			table.setHeaderRows(table.getHeaderRows()+1);
		}
	}



	public File build(ResultSet rs) throws DocumentException, SQLException, IOException {

		PdfPTable table;

		Document document = new Document(PageSize.A4.rotate(), 5, 5, 25, 25);
		tmpFile = File.createTempFile("tmp", ".pdf");
		FileOutputStream fos = new FileOutputStream(tmpFile);		
		PdfWriter.getInstance(document, fos);
//		PrintWriter writer = new PrintWriter(new FileOutputStream(tmpFile));
		document.open();

		// Creazione della lista delle colonne...
		List<String> fields = new ArrayList<String>();
		// ...se disponibile utilizzando l'header ed i nomi dei campi del ResultSet forniti
		if (dataColumns != null) {
			for (String header : dataHeader) {
				fields.add(dataColumns.get(header));
			}
		} else {
			// ...altrimenti viene effettuato un recupero automatico dei campi del ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				fields.add(rsmd.getColumnName(i));
			}
		}


		table = new PdfPTable(fields.size());
		table.setComplete(false);
		
		if (dataHeaderWidths != null) {
			table.setWidths(dataHeaderWidths);
		}
		
		
		buildPageHeader(table);
		buildDataHeader(table);


		// Cicla gli oggetti della lista per stamparne uno per riga
		String cellContent;
		while (rs.next()) {
			// Cicla l'array con i nomi dei metodi e tramite reflection stampa in ogni cella della riga il dato opportuno controllando anche se si tratta di un numero
			for (String field : fields) {
				try {
					cellContent = rs.getString(field);
					table.addCell(createCell(cellContent, FontFactory.COURIER, 7, 1, -1, null, null));
					if (Runtime.getRuntime().freeMemory() < 200000) {
						document.add(table);
						System.out.println(MemoryManager.cleanMemory(5));
						fos.flush();
					}
				} catch (OutOfMemoryError E) {
					System.out.println("Fine memoria: " + Runtime.getRuntime().freeMemory() + " riga " + rs.getRow());
				}
			}
		}

		table.setComplete(true);
		table.setWidthPercentage(100);
		document.add(table);
		document.close();

		return tmpFile;
	}


	public void toFile(String filename) throws DocumentException, IOException {
//		Document document = new Document(PageSize.A4.rotate(), 5, 5, 25, 25);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		PdfReader reader = new PdfReader(tmpFile.getAbsolutePath());
		int n = reader.getNumberOfPages();
		PdfImportedPage page;
		for (int i = 1; i <= n; i++) {
			page = writer.getImportedPage(reader, i);
			Image instance = Image.getInstance(page);
			document.add(instance);
		}
		document.close();
	}



	private PdfPCell createCell(String txt, String font, int dim, int colspan, int align, BaseColor borderColor, BaseColor backgroundColor) {
		PdfPCell c = new PdfPCell(new Phrase(txt, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL)));
		// , new Color(0, 0, 0)
		if (colspan != -1)
			c.setColspan(colspan);
		if (align != -1)
			c.setHorizontalAlignment(align);
		// c.setBorder(marg);
		if (borderColor != null)
			c.setBorderColor(borderColor);
		if (backgroundColor != null)
			c.setBackgroundColor(backgroundColor);

		return c;
	}




}