package it.feio.utils.net;


import it.autostrade.bmt.util.log4jutil.LogStackTrace;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

/**
 * Classe che implementa la connessione ad un account FTP
 * 
 * usa la libreria Commons Net ver. 3.0.1
 * 
 * 
 * @author 93700005 (Alessandro Ponzanelli - ANANSI Team)
 * 
 */
public class FTPConnect {

	private Logger log;
	private String user;
	private String password;
	private String IPAddress;
	private FTPClient client;

	/**
	 * Connettore FTP
	 * 
	 * @param username
	 * @param password
	 * @param indirizzo
	 *              IP server
	 */
	public FTPConnect(String user, String password, String IPAddress) {
		log = Logger.getLogger(this.getClass().getName());
		this.user = user;
		this.password = password;
		this.IPAddress = IPAddress;
		client = new FTPClient();
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	/**
	 * Implementa la connessione al server FTP
	 * 
	 * @return result Esito della connessione
	 * @throws FTPConnectionClosedException
	 * @throws IOException
	 */
	public boolean connect() throws FTPConnectionClosedException, IOException {
		boolean result;
		try {
			// Mi connetto al server FTP
			client.connect(IPAddress);
			// Tento la login al server FTP
			if (!client.login(user, password)) {
				// Se non avviene con successo eseguo il logout
				client.logout();
				result = false;
			}
			// Se è tutto OK
			else {
				// Cambio la modalità
				client.enterLocalPassiveMode();
				// Setto il tipo di file che sarà trasferito
				client.setFileType(FTP.BINARY_FILE_TYPE);
				log.info("=====Connessione con host " + IPAddress + " avvenuta con successo...=====");
				result = true;
			}
		} catch (FTPConnectionClosedException e) {
			log.error("Connessione chiusa dal server...");
			log.error(e.getMessage());
			result = false;
			throw e;
		} catch (IOException e) {
			log.error("Errore di IO...");
			log.error(e.getMessage());
			result = false;
			throw e;
		}

		return result;
	}

	/**
	 * Implementa la disconnessione dal server
	 * 
	 * @return result Esito della disconnessione
	 * @throws IOException
	 */
	public boolean disconnect() throws IOException {
		boolean result;
		// Eseguo la disconnect
		try {
			client.disconnect();
			result = true;
		} catch (IOException e) {
			log.error("Errore durante la disconnect da " + IPAddress + "...");
			log.error(e.getMessage());
			result = false;
			throw e;
		}
		if (result)
			log.info("=====Disconnessione da " + IPAddress + " avvenuta con successo...=====");
		return result;
	}

	/**
	 * Implementa la logout dall'account FTP
	 * 
	 * @return result Esito della logout del server FTP
	 * @throws FTPConnectionClosedException
	 * @throws IOException
	 */
	public boolean logout() throws FTPConnectionClosedException, IOException {
		boolean result;
		// Eseguo la logout
		try {
			result = client.logout();
		} catch (FTPConnectionClosedException e) {
			log.error("Errore durante la logout da " + IPAddress + "...");
			log.error(e.getMessage());
			result = false;
			throw e;
		} catch (IOException e) {
			log.error("Errore durante la logout da " + IPAddress + "...");
			log.error(e.getMessage());
			result = false;
			throw e;
		}
		if (result)
			log.info("Logout da " + IPAddress + " avvenuta con successo...");
		return result;
	}


	/**
	 * Ritorna una lista dei file presenti nel path remoto indicato
	 * 
	 * @author 17000026
	 * @param path
	 *              Percorso di cui è richiesto il contenuto
	 * @return Array di FTPFile con il listato
	 * @throws IOException
	 */
	public FTPFile[] list(String path) throws IOException {
		FTPFile[] list = null;
		list = client.listFiles(path);
		return list;
	}


	/**
	 * Implementa il download di un file dall'account FTP
	 * 
	 * @author 17000026
	 * @param path
	 *              percorso della directory in cui è contenuto il file da scaricare
	 * @param dstPath
	 *              percorso della directory locale in cui deve essere salvato il file
	 * @param fileName
	 *              nome del file da scaricare (il file scaricato sarà anche cancellato dallo spazio FTP)
	 * @param deleteFoundfFile
	 *              [true -> cancella il file trovato dallo spazio FTP / false -> il file non viene cancellato dallo spazio FTP]
	 * @return esito
	 */
	public boolean getFile(String path, String fileName, String dstPath, boolean deleteFoundFile) throws IOException, FileNotFoundException {
		boolean result = false;
		// Di default quando si chiama la getFile, il file scaricato viene cancellato, di conseguenza non sarà eseguita la rinomina
		result = getFile(path, fileName, dstPath, deleteFoundFile, false, fileName);

		return result;
	}

	/**
	 * Implementa il download di un file da un server FTP ricercandolo tramite espressione regolare
	 * 
	 * @author 17000026
	 * @param path
	 *              percorso della directory in cui è contenuto il file da scaricare
	 * @param dstPath
	 *              percorso della directory locale in cui deve essere salvato il file
	 * @param fileName
	 *              nome del file da scaricare (il file scaricato sarà anche cancellato dallo spazio FTP)
	 * @param deleteFoundfFile
	 *              [true -> cancella il file trovato dallo spazio FTP / false -> il file non viene cancellato dallo spazio FTP]
	 * @param regex
	 *              Indica se si sta usando un'espressione regolare per indicare il file da scaricare
	 * @return esito
	 */
	public boolean getFile(String path, String fileName, String dstPath, boolean deleteFoundFile, boolean regex) throws IOException, FileNotFoundException {
		boolean result = false;
		String fName = "";
		FTPFile[] list = list(path);
		for (FTPFile f : list) {
			fName = f.getName();
			if (fName.matches(fileName)) {
				result = (result || getFile(path, fName, dstPath, deleteFoundFile));
			}
		}
		return result;
	}

	/**
	 * Implementa il download di un file dall'account FTP
	 * 
	 * @param path
	 *              percorso della directory in cui è contenuto il file da scaricare
	 * @param dstPath
	 *              percorso della directory locale in cui deve essere salvato il file
	 * @param fileName
	 *              nome del file da scaricare (il file scaricato sarà anche cancellato dallo spazio FTP)
	 * @return esito
	 */
	public boolean getFile(String path, String fileName, String dstPath) throws IOException, FileNotFoundException {
		boolean result = false;
		result = getFile(path, fileName, dstPath, true, false, fileName);
		return result;
	}

	/**
	 * Implementa il download di un file dall'account FTP
	 * 
	 * @param path
	 *              percorso della directory in cui è contenuto il file da scaricare
	 * @param dstPath
	 *              percorso della directory locale in cui deve essere salvato il file
	 * @param fileName
	 *              nome del file da scaricare
	 * @param deleteFoundfFile
	 *              [true -> cancella il file trovato dallo spazio FTP / false -> il file non viene cancellato dallo spazio FTP]
	 * @param rename
	 *              [true -> una volta scaricato (se il parametro precedente non è true) il file viene rinominato / false -> il file non viene rinominato]
	 * @param newFileName
	 *              nuovo nome del file con cui dovrà essere rinominato il file contenuto nello spazio FTP
	 * @return esito
	 */
	public boolean getFile(String path, String fileName, String dstPath, boolean deleteFoundFile, boolean rename, String newFileName) throws IOException, FileNotFoundException {
		OutputStream fos = null;
		boolean cambioDir = true, result = false;
		String dirAttuale = "";
		StringTokenizer st = new StringTokenizer(path);

		try {
			client.changeWorkingDirectory("/");
		} catch (IOException e) {
			log.error(e.getMessage());
			throw e;
		}

		// Se il file si trova all'interno di una sottodirectory
		while (st.hasMoreTokens()) {
			try {
				// Passo alla directory successiva
				cambioDir = client.changeWorkingDirectory(st.nextToken("/"));
				// Se è andato a buon fine il cambio directory
				if (cambioDir) {
					dirAttuale += client.printWorkingDirectory() + "/";
					log.trace("Directory attuale " + dirAttuale);
				}
			} catch (IOException e) {
				log.error("Impossibile cambiare la directory...");
				log.error(e.getMessage());
				cambioDir = false;
				throw e;
			}
		}
		// Se tutti i cambi di directory sono andati a buon fine
		if (cambioDir) {
			try {
				// Creo un output stream per scaricare il file
				fos = new FileOutputStream(dstPath + "/" + fileName);
				log.trace("Download del file " + dstPath + fileName + " in corso...");
				log.debug("(FTP)" + dirAttuale + fileName + ">>>>>>(LOCALE)" + dstPath + fileName);
				// Scarico il file
				client.retrieveFile(fileName, fos);
				// Chiudo l'output stream
				fos.close();
				// TUTTO OK
				result = true;
				log.info("File " + dstPath + fileName + " scaricato...");
			} catch (FileNotFoundException e) {
				log.warn("File " + dstPath + fileName + " non trovato...");
				log.warn(e.getMessage());
				throw e;
			} catch (IOException e) {
				log.error("File " + dstPath + fileName + " non scaricato...");
				log.error(e.getMessage());
				throw e;
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					log.error("Errore di IO in chiusura OutputStream...");
					log.error(e.getMessage());
					throw e;
				}
			}
		}
		// Se è tutto OK
		if (result) {
			log.info("Il file deve essere cancellato una volta scaricato? " + (deleteFoundFile ? "Si" : "No"));
			if (deleteFoundFile) {
				try {
					// Cancello il file
					if (client.deleteFile(fileName)) {
						log.info("File " + fileName + " scaricato e rimosso...");
					} else {
						log.warn("File " + fileName + " scaricato ma non rimosso...");
					}
				} catch (IOException e) {
					log.error("Impossibile eliminare il file " + path + "/" + fileName + " ...");
					log.error(e.getMessage());
					throw e;
				}
			} else {
				log.info("File " + fileName + " scaricato e non rimosso...");
				log.info("Il file deve essere rinominato una volta scaricato? " + (rename ? "Si" : "No"));
				if (rename) {
					try {
						if (client.rename(fileName, newFileName)) {
							log.info("File " + fileName + " rinominato in " + newFileName);
						} else {
							log.warn("File " + fileName + " non rinominato...");
						}
					} catch (IOException e) {
						log.error("Impossibile rinominare il file " + path + "/" + fileName + " in " + newFileName);
						log.error(e.getMessage());
						throw e;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Scarica una lista di file dall'account FTP
	 * 
	 * @param path
	 *              percorso sorgente dei files
	 * @param fileNames
	 *              lista contenente i nomi dei files da scaricare
	 * @param dstPath
	 *              percorso destinazione dei files
	 * @return esito esito = false se non riesce a scaricare TUTTI i files
	 */
	public boolean getFiles(String path, List<String> fileNames, String dstPath) throws IOException {
		boolean fileGot = false;

		for (String fileName : fileNames) {
			try {
				fileGot = getFile(path, fileName, dstPath);
			} catch (IOException e) {
				LogStackTrace.traceStackTrace(log, e.getStackTrace());
				throw e;
			}
			if (!fileGot) {
				log.warn("Impossibile scaricare il file " + fileName + "...");
				break;
			}
		}
		return fileGot;
	}

	/**
	 * Controlla l'esistenza di un file all'interno dello spazio FTP
	 * 
	 * @param fileName
	 *              nome del file da cercare
	 * @return trovato
	 */
	public boolean exist(String path, String fileName) throws IOException {
		boolean cambioDir = true, trovato = false;
		FTPFile[] files = null;
		StringTokenizer st = new StringTokenizer(path);
		@SuppressWarnings("unused")
		String dirAttuale = "";

		try {
			client.changeWorkingDirectory("/");
		} catch (IOException e) {
			log.error("Errore nel cambio della directory...");
			log.error(e.getMessage());
			throw e;
		}

		while (st.hasMoreTokens()) {
			try {
				cambioDir = client.changeWorkingDirectory(st.nextToken("/"));
				if (cambioDir) {
					dirAttuale += client.printWorkingDirectory() + "/";
				}
			} catch (IOException e) {
				log.error("Impossibile cambiare la directory...");
				log.error(e.getMessage());
				cambioDir = false;
				throw e;
			}
		}
		try {
			log.trace("Cerco il file " + fileName + " nella directory: " + client.printWorkingDirectory());
			// Ottengo la lista dei file presenti nella directory
			files = client.listFiles();
		} catch (IOException e) {
			log.error("Impossibile reperire la lista di files nella directory FTP ");
			log.error(e.getMessage());
			trovato = false;
			throw e;
		}
		// Cerco all'interno dei file della directory la presenza del file
		for (FTPFile file : files) {
			if (file.getName().equals(fileName)) {
				trovato = true;
				log.debug("File: " + fileName + " trovato...");
			}
		}
		if (!trovato)
			log.warn("File " + fileName + " non trovato...");
		return trovato;
	}

	/**
	 * Implementa l'upload di un file dall'account FTP
	 * 
	 * @param percorso
	 *              della directory in cui è contenuto il file da caricare
	 * @param nome
	 *              del file da caricare
	 * @return esito
	 */
	public boolean putFile(String path, String fileName) throws FileNotFoundException, IOException {
		FileInputStream fis = null;
		boolean result;

		try {
			fis = new FileInputStream(path + "/" + fileName);
			client.storeFile(fileName, fis);
			fis.close();
			result = true;
			log.info("File " + fileName + " caricato con successo in " + path + "...");
		} catch (FileNotFoundException e) {
			log.warn("File " + fileName + " non trovato...");
			log.warn(e.getMessage());
			result = false;
			throw e;
		} catch (IOException e) {
			log.error("Errore di IO...");
			log.error(e.getMessage());
			result = false;
			throw e;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				log.error("Errore di IO in chiusura OutputStream...");
				log.error(e.getMessage());
				result = false;
				throw e;
			}
		}
		return result;
	}


	/**
	 * Sposta un file remoto tra due directory di un server FTP
	 * 
	 * @author 17000026
	 * @param srcPath
	 *              Percorso di origine del file
	 * @param targetPath
	 *              Percorso di destinazione del file
	 * @param fileName
	 *              Nome del file da spostare
	 * @return Vero se l'operazione è riuscita, falso altrimenti
	 * @throws IOException
	 */
	public boolean moveFile(String srcPath, String targetPath, String fileName) throws IOException {
		boolean result;
		if (client.rename("/" + srcPath + "/" + fileName, "/" + targetPath + "/" + fileName))
			result = true;
		else
			result = false;
		return result;
	}


	/**
	 * Cancella un file remoto dal server
	 * 
	 * @author 17000026
	 * @param path
	 *              Percorso del file
	 * @param fileName
	 *              Nome del file da cancellare
	 * @return Vero se l'operazione è riuscita, falso altrimenti
	 * @throws IOException
	 */
	public boolean deleteFile(String path, String fileName) throws IOException {
		boolean result;
		if (client.deleteFile("/" + path + "/" + fileName))
			result = true;
		else
			result = false;
		return result;
	}



}
