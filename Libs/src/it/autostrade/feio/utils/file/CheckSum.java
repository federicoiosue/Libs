package it.autostrade.feio.utils.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 06/feb/2012 Utilità per la gestione di hash di file e stringhe
 */
public class CheckSum {


	/**
	 * @param fileName
	 *              Path completo del file di cui si vuole ottenere l'hash
	 * @return Stringa contenente l'hash del file
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getMd5(String fileName) throws NoSuchAlgorithmException, IOException {

		// Definizione algoritmo, creazione streaming per il file di input e inizializzazione variabili
		MessageDigest md = MessageDigest.getInstance("SHA1");
		FileInputStream fis = new FileInputStream(fileName);
		byte[] dataBytes = new byte[1024];
		int nread = 0;

		// Lettura del file ed aggiornamento dello stato del digest
		while ((nread = fis.read(dataBytes)) != -1)
			md.update(dataBytes, 0, nread);
		byte[] mdbytes = md.digest();

		// Conversione del digest in esadecimale
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}


	/**
	 * @param fileNameA
	 *              Primo dei file da confrontare
	 * @param fileNameB
	 *              Secondo dei file da confrontare
	 * @return Vero se i file combaciano, falso altrimenti
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static boolean compare(String fileNameA, String fileNameB) throws NoSuchAlgorithmException, IOException {

		// Inizializzazione variabili
		boolean areEquals = false;
		String md5a, md5b;

		// Calcolo dell'ash dei singoli file da confrontare
		md5a = getMd5(fileNameA);
		md5b = getMd5(fileNameB);

		// Comparazione e ritorno del risultato
		if (md5a.equals(md5b))
			areEquals = true;
		return areEquals;
	}


	/**
	 * @param fileNames
	 *              Array con i nomi dei file da confrontare
	 * @return Vero se i file sono tutti uguali, falso altrimenti
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static boolean compare(String[] fileNames) throws NoSuchAlgorithmException, IOException {

		// Inizializzazione variabili
		boolean areEquals = true;
		String md5Final = "", md5Single;

		// Calcolo dell'ash dei singoli file da confrontare
		for (String fileName : fileNames) {
			md5Single = getMd5(fileName);
			if (md5Final == "") {
				md5Final = md5Single;
			} else {
				if (!md5Final.equals(md5Single))
					areEquals = false;
			}
		}
		return areEquals;
	}

}
