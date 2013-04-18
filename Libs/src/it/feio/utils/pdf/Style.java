package it.feio.utils.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;



/**
 * Cells styles for PDF exporting.
 * All the properties added to the class attributes Font and PdfCell are automatically used in the implementation.
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 16/apr/2013
 *
 */
public class Style {
	
	final float DEFAULT_BORDER = 0;

	private Font f;
	private PdfPCell c;

	public Style() {
		f = new Font();
		c = new PdfPCell();
		setBorders("", DEFAULT_BORDER, DEFAULT_BORDER, DEFAULT_BORDER, DEFAULT_BORDER);
	}


	public Font getFont() {
		return f;
	}

	public PdfPCell getCell() {
		return c;
	}

	public Style setFont(String font) {
		f.setFamily(font);
		return this;
	}

	public Style setSize(int size) {
		f.setSize(size);
		return this;
	}

	public Style setColor(String htmlColor) {
		f.setColor(WebColors.getRGBColor(htmlColor));
		return this;
	}

	public Style setAlignment(int horizontal, int vertical) {
		c.setHorizontalAlignment(horizontal);
		c.setVerticalAlignment(vertical);
		return this;
	}

	public Style setBackground(String htmlColor) {
		c.setBackgroundColor(WebColors.getRGBColor(htmlColor));
		return this;
	}

	public Style setBorders(String htmlColor, float left, float right, float top, float bottom) {
		if (htmlColor.equals("")) 
			htmlColor = "#000000";
		c.setBorderColor(WebColors.getRGBColor(htmlColor));
		c.setBorderWidthLeft(left);
		c.setBorderWidthRight(right);
		c.setBorderWidthTop(top);
		c.setBorderWidthBottom(bottom);
		return this;
	}


}