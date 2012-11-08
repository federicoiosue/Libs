package it.autostrade.feio.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Gestisce la sostituzione di stringhe o caratteri in un file.
 * 
 * @author Federico Iosue (17000026)
 * @version 0.1 (5 gennaio 2012)
 */
public class Replacer {



	private static File outFile;



	/**
	 * Sostituisce un set di caratteri tipici ISO-8859-1 con caratteri UTF-8 nel file passato come parametro
	 * 
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @return Handle al file conle stirnghe sostituite
	 */
	public static File replace(String fileOriginal) {
		String fileTarget = fileOriginal;
		return replace(fileOriginal, fileTarget);
	}



	/**
	 * Sostituisce un set di caratteri tipici ISO-8859-1 con caratteri UTF-8 in una copia del file passato come parametro
	 * 
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @param fileTarget
	 *              il nome del nuovo file da creare partendo dall'originale e nel quale effettuare la sostituzione
	 * @return Handle al file con le stringhe sostituite
	 */
	public static File replace(String fileOriginal, String fileTarget) {
		String[] pattern = { "à", "Ã ", "è", "Ã¨", "é", "Ã©", "ì", "Ã¬", "ò", "Ã²", "ù", "Ã¹", "°" };
		String[] replacement = { "a", "a", "e", "e", "e", "e", "i", "i", "o", "o", "u", "Ã¹", "gradi" };
		return replace(pattern, replacement, fileOriginal, fileTarget);
	}



	/**
	 * Sostituisce un carattere o stringha definito dal chiamante nel del file passato come parametro
	 * 
	 * @param pattern
	 *              il carattere o stringa da cercare
	 * @param replacement
	 *              il carattere o stringa da sostituire
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @return Handle al file con le stringhe sostituite
	 */
	public static File replace(String pattern, String replacement, String fileOriginal) {
		String fileTarget = fileOriginal;
		String[] patternArray = { pattern };
		String[] replacementArray = { replacement };
		return replace(patternArray, replacementArray, fileOriginal, fileTarget);
	}



	/**
	 * Sostituisce un set di caratteri o stringhe definiti dal chiamante nel del file passato come parametro
	 * 
	 * @param pattern
	 *              il carattere o stringa da cercare
	 * @param replacement
	 *              il carattere o stringa da sostituire
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @return Handle al file con le stringhe sostituite
	 */
	public static File replace(String[] pattern, String[] replacement, String fileOriginal) {
		String fileTarget = fileOriginal;
		return replace(pattern, replacement, fileOriginal, fileTarget);
	}



	/**
	 * Sostituisce un carattere o stringha definito dal chiamante in una copia del file passato come parametro
	 * 
	 * @param pattern
	 *              il carattere o stringa da cercare
	 * @param replacement
	 *              il carattere o stringa da sostituire
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @return Handle al file con le stringhe sostituite
	 */
	public static File replace(String pattern, String replacement, String fileOriginal, String fileTarget) {
		String[] patternArray = { pattern };
		String[] replacementArray = { replacement };
		return replace(patternArray, replacementArray, fileOriginal, fileTarget);
	}



	/**
	 * Sostituisce un set di caratteri o stringhe definiti dal chiamante in una copia del file passato come parametro
	 * 
	 * @param array
	 *              di caratteri o stringhe da cercare
	 * @param array
	 *              di caratteri o stringhe da sostituire
	 * @param fileOriginal
	 *              il nome del file nel quale effettuare la sostituzione
	 * @param fileTarget
	 *              il nome del nuovo file da creare partendo dall'originale e nel quale effettuare la sostituzione
	 * @return Handle al file con le stringhe sostituite
	 */
	public static File replace(String[] pattern, String[] replacement, String fileOriginal, String fileTarget) {
		boolean rename = false;

		// Crea un nome per il file temporaneo sul quale lavorare e imposta un flag per effettuare una rinominazione alla fine del metodo
		if (fileOriginal == fileTarget) {
			fileTarget = fileTarget + "_" + new Date().getTime() + ".tmp";
			rename = true;
		}

		outFile = new File(fileTarget);

		// Controlla che gli elementi del patter e del replacement abbiano un rapporto uno a uno
		if (pattern.length != replacement.length)
			return null;

		String str;

		// Effettua la lettura riga per riga del file in input e salva le righe con i pattern eventuali trovati sistituiti nel file temporaneo
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileOriginal)));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
			while ((str = in.readLine()) != null) {
				for (int i = 0; i < pattern.length; i++) {
					str = str.replaceAll(pattern[i], replacement[i]);
				}
				out.write(str + System.getProperty("line.separator"));
			}
			in.close();
			out.close();
		} catch (IOException ioe) {
			return null;
		}

		// Rinomina il file temporaneo col nome del file in input se il metodo chiamato prevedeva la sovrascrittura, altrimenti li lascia entrambi sul fs
		if (rename) {
			try {
				File original = new File(fileOriginal);
				original.delete();
				outFile.renameTo(original);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		// Ritorna un handle al file con i pattern sostituiti
		return outFile;
	}
}
