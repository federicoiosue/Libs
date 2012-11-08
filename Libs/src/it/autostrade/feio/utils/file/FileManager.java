package it.autostrade.feio.utils.file;

import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 * Classe per la gestione dei file in locale.
 * 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 31/ott/2012
 *
 */
public class FileManager {


	public FileManager() {}


	/**
	 * Scrive un file da un InputStream.
	 * 
	 * @param is
	 *              sorgente
	 * @param nomeFile
	 *              nome file destinazione
	 * @return true se l'operazione riesce, false altrimenti
	 */
	public static boolean write(InputStream is, String nomeFile) throws UnsupportedEncodingException, IOException {
		boolean result = false;
		if (is != null) {
			String line = null;
			BufferedReader reader = null;
			PrintWriter file = null;
				file = new PrintWriter(new FileWriter(nomeFile));
				reader = new BufferedReader(new InputStreamReader(is, "US-ASCII"));
				if (reader != null) {
					while ((line = reader.readLine()) != null) {
						file.println(line);
					}
				}
				result = true;
				if (is != null)
					is.close();
				if (reader != null)
					reader.close();
				if (file != null)
					file.close();
				return result;
		}
		return result;
	}


	/**
	 * Scrive in un file partendo da una stringa di testo.
	 * 
	 * @author 17000026 (Federico Iosue Sistemi&Servizi) 13/01/2012
	 * @param str
	 *              Stringa da scrivere su file
	 * @param nomeFile
	 *              Nome del file sul quale scrivere
	 * @return true se l'operazione riesce, false altrimenti
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static boolean write(String str, String nomeFile) throws UnsupportedEncodingException, IOException {
		boolean result;
		InputStream is;
		try {
			is = new ByteArrayInputStream(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw (e);
		}
		try {
			result = write(is, nomeFile);
		} catch (UnsupportedEncodingException ee) {
			throw (ee);
		} catch (IOException ioe) {
			throw (ioe);
		}
		return result;
	}


	/**
	 * Scrive in un file partendo da una lista di stringhe.
	 * 
	 * @author 17000026 (Federico Iosue Sistemi&Servizi) 13/01/2012
	 * @param list
	 *              Lista di stringhe da scrivere su file
	 * @param nomeFile
	 *              Nome del file sul quale scrivere
	 * @param newLine
	 *              Se scrivere gli elementi della lista uno per riga
	 * @return true se l'operazione riesce, false altrimenti
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static boolean write(List<String> list, String nomeFile, boolean newLine) throws UnsupportedEncodingException, IOException {
		boolean result = false;
		String listString = "";
		Iterator<String> i = list.iterator();
		while (i.hasNext()) {
			listString += (String) i.next();
			if (newLine)
				listString += System.getProperty("line.separator");
		}
		try {
			result = write(listString, nomeFile);
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return result;
	}


	/**
	 * Metodo che rinomina un file.
	 * 
	 * @param path
	 *              percorso file
	 * @param nomeFile
	 *              nome file da rinominare
	 * @param newNomeFile
	 *              nuovo nome del file
	 * @return true se l'operazione riesce, false altrimenti
	 */
	public static boolean rename(String path, String nomeFile, String newNomeFile) {
		File f = new File(path + nomeFile);
		return f.renameTo(new File(path + newNomeFile));
	}

	/**
	 * Metodo per spostare un file. Il file viene anche rinominato.
	 * 
	 * @param nomeFile
	 *              nome del file da spostare
	 * @param nuovoNome
	 *              nuovo nome del file
	 * @param dirFile
	 *              percorso del file da spostare
	 * @param dirMove
	 *              percorso destinazione del file
	 * @return true se l'operazione riesce, false altrimenti
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean move(String nomeFile, String nuovoNome, String dirFile, String dirMove) throws FileNotFoundException, IOException {
		boolean result = true;
		if (nuovoNome == null)
			nuovoNome = nomeFile;
		InputStream file;		
		file = new FileInputStream(dirFile + nomeFile);
		OutputStream out = new FileOutputStream(dirMove + nuovoNome);
		byte[] buf = new byte[1024];
		int len;
		while ((len = file.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		file.close();
		out.close();
		File fileC = new File(dirFile + nomeFile);
		fileC.delete();
		return result;
	}

	/**
	 * Metodo per spostare un file.
	 * 
	 * @param nomeFile
	 *              nome file da spostare
	 * @param dirFile
	 *              percorso del file da spostare
	 * @param dirMove
	 *              percorso destinazione del file
	 * @return true se l'operazione riesce, false altrimenti
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean move(String nomeFile, String dirFile, String dirMove) throws FileNotFoundException, IOException {
		return move(nomeFile, null, dirFile, dirMove);		
	}
	
	
	/**
	 * Sposta o rinomina un file in un altro
	 * @param fileFrom
	 * @param fileTo
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean move(File fileFrom, File fileTo) throws FileNotFoundException, IOException {
		String fileFromPath = fileFrom.getAbsolutePath().substring(0,fileFrom.getAbsolutePath().lastIndexOf(File.separator) + 1);
		String fileToPath = fileTo.getAbsolutePath().substring(0,fileTo.getAbsolutePath().lastIndexOf(File.separator) + 1);
		return move(fileFrom.getName(), fileTo.getName(), fileFromPath, fileToPath);		
	}

	
}
