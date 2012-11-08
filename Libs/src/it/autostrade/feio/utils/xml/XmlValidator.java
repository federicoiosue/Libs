package it.autostrade.feio.utils.xml;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Verifica la validità di un documento xml a partire dal suo schema xsd.
 * 
 * @author Federico Iosue (17000026)
 * @version 0.1 (5 gennaio 2012)
 */
public class XmlValidator {

	private static String xmlSchema = "http://www.w3.org/2001/XMLSchema";

	/**
	 * Effettua la validazione del documento xml basandosi sul suo xsd
	 * 
	 * @param schemaFileName
	 *              Il nome del file contenente lo schema
	 * @param instanceFileName
	 *              Il nome del file xml da validare
	 * @return Un hashmap contenente il risultato della validazione nella forma {risultato=descrizione}
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashMap<Boolean, String> validate(String schemaFileName, String instanceFileName) throws SAXException, IOException {
		// Inizializzazione variabili
		HashMap<Boolean, String> result = new HashMap<Boolean, String>();
		// Istanzia le classi per il parsing del file xml
		SchemaFactory factory = SchemaFactory.newInstance(xmlSchema);
		Schema schema = factory.newSchema(new File(schemaFileName));
		javax.xml.validation.Validator validator = schema.newValidator();
		Source source = new StreamSource(instanceFileName);

		// Effettua la validazione dell'xml con l'xsd
		try {
			validator.validate(source);
			result.put(true, "La validazione del file " + instanceFileName + " è avvenuta correttamente");
		} catch (SAXParseException ex) {
			// In caso di mancata validazione un messaggio informerà di dove si è presentato un problema
			result.put(false, "File " + instanceFileName + " non ben formato (linea " + ex.getLineNumber() + ", colonna " + ex.getColumnNumber() + ": " + ex.getMessage() + ")");
		}
		// Ritorna il risultato della validazione
		return result;
	}

	/**
	 * Effettua la validazione di più documenti xml basandosi sul loro comune xsd
	 * 
	 * @param schemaFileName
	 *              Il nome del file contenente lo schema comune alle istanze xml
	 * @param instanceFileNames
	 *              Array con il nomi dei files xml da validare
	 * @return Un hashmap contenente il risultato della validazione nella forma {risultato=descrizione}
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashMap<Boolean, String> validate(String schemaFileName, String[] instanceFileNames) throws SAXException, IOException {
		// Inizializzazione variabili
		HashMap<Boolean, String> result = new HashMap<Boolean, String>();
		// Ciclo per ogni xml passato come parametro al metodo
		for (String instanceFileName : instanceFileNames) {
			// Effettua la validazione dell'xml con l'xsd ed ottiene il risultato parziale
			try {
				result.putAll(XmlValidator.validate(schemaFileName, instanceFileName));
				if (result.containsKey(false)) {
					return result;
				}
			} catch (SAXException se) {
				System.out.println(se.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			result.clear();
			result.put(true, "La validazione dei file " + Arrays.toString(instanceFileNames).replace("[", "").replace("]", "") + " è avvenuta correttamente");
		}
		// Ritorna il risultato della validazione
		return result;

	}
}




