package it.feio.utils.pdf;

import it.feio.utils.obj.MemoryManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Simple PDF exporting class.
 * It's useful for automating the management of the column to be exported and the styles of the document.
 * In its simplier usage method its just possible to create an instance and pass it a resultset to get pdf document printed.  
 * The pdf is directly written in a temporary file, this way is possible to flush the data and try to keep some memory free for 
 * extensive computing documents.
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 16/apr/2013
 *
 */
public class PdfExporter extends PdfPageEventHelper {
	
	final Rectangle DEFAULT_PAGE_SIZE = PageSize.A4;
	final int DEFAULT_MARGIN_LEFT = 5;
	final int DEFAULT_MARGIN_RIGHT = 5;
	final int DEFAULT_MARGIN_TOP = 25;
	final int DEFAULT_MARGIN_BOTTOM = 25; 

	private Document doc;
	private String file;
	private String pageHeader;
	private String pageFooter;
	private String[] dataHeader;
	private Map<String, String> dataColumns;
	private int[] dataHeaderWidths;
	private Style pageHeaderStyle;
	private Style dataHeaderStyle;
	private Style dataStyle;
	private PdfPCell pageHeaderCell;
	private PdfPCell pageFooterCell;
	private PdfTemplate documentPages;
	private String defaultLinkString;



	/**
	 * Default constructor with predefined page size and margins
	 */
	public PdfExporter(String file) {
		this.file = file;
		doc = new Document(DEFAULT_PAGE_SIZE, DEFAULT_MARGIN_LEFT, DEFAULT_MARGIN_RIGHT, DEFAULT_MARGIN_TOP, DEFAULT_MARGIN_BOTTOM);
	}


	/**
	 * Se page size
	 * @param pagesize
	 * @return
	 */
	public PdfExporter setPageSize(Rectangle pagesize) {
		doc.setPageSize(pagesize);
		return this;
	}
	
	
	public void setDefaultLinkString(String defaultLinkString) {
		this.defaultLinkString = defaultLinkString;
	}

	
	/**
	 * Set page margins
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 * @return
	 */
	public PdfExporter setPageMargins(int left, int right, int top, int bottom) {
		doc.setMargins(left, right, top, bottom);
		return this;
	}

	/**
	 * Imposta un'intestazione testuale per ogni pagina del documento
	 * 
	 * @param pageHeader
	 *              Stringa con l'intestazione
	 */
	public void setPageFooter(String pageFooter) {
		this.pageFooter = pageFooter;
	}

	/**
	 * Imposta un'intestazione testuale per ogni pagina del documento
	 * 
	 * @param pageHeader
	 *              Stringa con l'intestazione
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
			@SuppressWarnings("rawtypes")
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

	
	/**
	 * Set data table header fixed width
	 * @param dataHeaderWidths
	 */
	public void setDataHeaderWidths(int[] dataHeaderWidths) {
		this.dataHeaderWidths = dataHeaderWidths;
	}


	/**
	 * Set data table header style
	 * @param style
	 */
	public void setDataHeaderStyle(Style style) {
		this.dataHeaderStyle = style;
	}


	
	private void buildPageHeaderFooter(PdfWriter writer) {
		if (pageHeader != null || pageFooter != null) {
			if (pageHeader != null) {
				pageHeaderCell = createCell(pageHeader, pageHeaderStyle);
			}
			if (pageFooter != null) {
				pageFooterCell = createCell(pageHeader, pageHeaderStyle);
			}
			writer.setPageEvent(this);
		}
	}


	/**
	 * Set page header style
	 * @param style
	 */
	public void setPageHeaderStyle(Style style) {
		this.pageHeaderStyle = style;
	}


	/**
	 * Set data table elements' style
	 * @param style
	 */
	public void setDataStyle(Style style) {
		this.dataStyle = style;
	}



	private void buildDataHeader(PdfPTable table) {
		if (dataHeader != null) {
			PdfPCell c;
			for (String dataHeaderElement : dataHeader) {
				c = createCell(dataHeaderElement, dataHeaderStyle);
				table.addCell(c);
			}
			table.setHeaderRows(table.getHeaderRows() + 1);
		}
	}



	/**
	 * Build method
	 * @param rs
	 * @return
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void build(ResultSet rs) throws DocumentException, SQLException, IOException {

		PdfPTable table;
		
		FileOutputStream fos= new FileOutputStream(file);			
		PdfWriter writer = PdfWriter.getInstance(doc, fos);

		// Creation of column list
		List<String> fields = new ArrayList<String>();
		// If is available it will be used the header given by user
		if (dataColumns != null) {
			for (String header : dataHeader) {
				fields.add(dataColumns.get(header));
			}
		} else {
			// ...otherwise fields will be automatically recovered from resultset
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


		buildPageHeaderFooter(writer);
		buildDataHeader(table);
		
		doc.open();

		// Cycling objects from list to print them one for row
		String cellContent;
		while (rs.next()) {
			// Cicla l'array con i nomi dei metodi e tramite reflection stampa in ogni cella della riga il dato opportuno controllando anche se si tratta di un numero
			for (String field : fields) {
				try {
					cellContent = rs.getString(field);
					table.addCell(createCell(cellContent, dataStyle));
					if (Runtime.getRuntime().freeMemory() < 200000) {
						doc.add(table);
						System.out.println("Freed memory -> " + MemoryManager.cleanMemory(5)/1024 + "kb");
						fos.flush();
					}
				} catch (OutOfMemoryError E) {
					System.out.println("End of JVM memory -> " + Runtime.getRuntime().freeMemory() + " at row " + rs.getRow());
				}
			}
		}

		table.setComplete(true);
		table.setWidthPercentage(100);
		doc.add(table);
		doc.close();

	}

	
	/**
	 * Creation of PdfPCell and application of predetermined style.
	 * In case the flag "defaultLinkString" (@see defaultLinkString) is set to true a default string will be automatically used as text for parsed URLs.
	 * @param txt String with content
	 * @param style Predetermined style for cell
	 * @return
	 */
	private PdfPCell createCell(String txt, Style style) {
		PdfPCell c = style.getCell();
		Anchor p = new Anchor(txt, style.getFont());
		if (this.defaultLinkString != null  ) {
			try {
				// Trying to parse URLs
				new URL(txt);
				p = new Anchor(new Chunk(this.defaultLinkString, style.getFont()));
				p.setReference(txt);
			} catch (MalformedURLException e) {}			
		} 
		c.setPhrase(p);
		return c;
	}


	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		this.documentPages = writer.getDirectContent().createTemplate(30, 16);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table;
		if (pageHeader != null || pageFooter != null) {		
			
			float leftMargin = pageHeaderStyle.getMargins()[0];
			float rightMargin = pageHeaderStyle.getMargins()[1];
			float topMargin = pageHeaderStyle.getMargins()[2];
			float bottomMargin = pageHeaderStyle.getMargins()[3];
			float width = document.getPageSize().getWidth() - (document.leftMargin() + document.rightMargin());
			
			if (pageHeader != null) {
				table = new PdfPTable(1);
				table.setTotalWidth(width);
				table.addCell(pageHeaderCell);
				if (bottomMargin > 0) {
					PdfPCell c = new PdfPCell();
					c.setFixedHeight(bottomMargin);
					table.addCell(c);
				}
				table.writeSelectedRows(0, -1,  document.getPageSize().getLeft() + leftMargin,  document.getPageSize().getTop() - document.topMargin(), writer.getDirectContent());			
			}		
			if (pageFooter != null) {
				table = new PdfPTable(1);
				table.setTotalWidth(width);
				table.addCell(pageFooterCell);
				table.writeSelectedRows(0, -1,  document.getPageSize().getLeft() + leftMargin,  document.getPageSize().getBottom() + document.bottomMargin() + pageFooterCell.getHeight(), writer.getDirectContent());			
			}	
		}
	}

//	@Override
//	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {}
//	@Override
//	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {}
//	@Override
//	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3, Paragraph arg4) {}
//	@Override
//	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {}
//	@Override
//	public void onStartPage(PdfWriter arg0, Document arg1) {}
//	@Override
//	public void onChapter(PdfWriter arg0, Document arg1, float arg2, Paragraph arg3) {}
//	@Override
//	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {}
//	@Override
//	public void onCloseDocument(PdfWriter arg0, Document arg1) {}
//	@Override
//	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2, String arg3) {}
	


}














