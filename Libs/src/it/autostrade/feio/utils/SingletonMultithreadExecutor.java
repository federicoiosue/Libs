package it.autostrade.feio.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingletonMultithreadExecutor {

	Executor executor;

	/**
	 * Costruttore privato, in quanto la creazione dell'istanza deve essere controllata.
	 */
	private SingletonMultithreadExecutor() {
		this.executor = Executors.newFixedThreadPool(5);
	}

	/**
	 * La classe Contenitore viene caricata/inizializzata alla prima esecuzione di getInstance() ovvero al primo accesso a Contenitore.ISTANZA, ed in modo thread-safe. Anche l'inizializzazione dell'attributo statico, pertanto, viene serializzata.
	 */
	private static class Contenitore {
		private final static SingletonMultithreadExecutor ISTANZA = new SingletonMultithreadExecutor();
	}

	/**
	 * Punto di accesso al Singleton. Ne assicura la creazione thread-safe solo all'atto della prima chiamata.
	 * 
	 * @return il Singleton corrispondente
	 */
	public static SingletonMultithreadExecutor getInstance() {
		return Contenitore.ISTANZA;
	}


	public void execute(Runnable r) {
		executor.execute(r);
	}
}