package it.autostrade.feio.utils;


public class SimpleTickerTask implements Runnable {

	private String name;

	public SimpleTickerTask() {
		SingletonMultithreadExecutor sm = SingletonMultithreadExecutor.getInstance();
		sm.execute(this);
	}

	public SimpleTickerTask(String name) {
		this.name = name;
		SingletonMultithreadExecutor sm = SingletonMultithreadExecutor.getInstance();
		sm.execute(this);
	}

	public void run() {
		int i = 0;
		while (true) {
			System.out.println("ciao" + (name != null ? " da " + name : "") + " numero " + i++);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}