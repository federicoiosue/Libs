package it.feio.utils.test;

import it.feio.utils.xml.XmlValidator;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TestXmlValidator {



	public static void main(String[] args) {

		// String schemaFileName = args[0];
		// String instanceFileName = args[1];
		String schemaFileName = "C:\\Documents and Settings\\17000026\\Documenti\\web\\ftp\\bollettini\\bollettini.xsd";
		String instanceFileName1 = "C:\\Documents and Settings\\17000026\\Documenti\\web\\ftp\\bollettini\\locale.xml";
		String instanceFileName2 = "C:\\Documents and Settings\\17000026\\Documenti\\web\\ftp\\Siti_Autostrade_12.xml";
		String[] arr = { instanceFileName1, instanceFileName2 };
		try {
			System.out.print(XmlValidator.validate(schemaFileName, instanceFileName1));
		} catch (SAXException se) {
			System.out.println(se.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}
