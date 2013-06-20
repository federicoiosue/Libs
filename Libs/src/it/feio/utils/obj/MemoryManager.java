package it.feio.utils.obj;


/**
 * Utility to force memory garbaging.
 * Used in intense memory usage elaboration without the necessity  of performances.
 * All measurements are made in bytes.
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 19/apr/2013
 *
 */
public class MemoryManager { 

	public static long cleanMemory(long percentage) {
		Long startingMem = getMemoryUsage();
		double freeMemory = (double) Runtime.getRuntime().freeMemory();
		double totalMemory = (double) Runtime.getRuntime().totalMemory();
		if (freeMemory / totalMemory * 100 < percentage) {
			garbageCollect();
			garbageCollect();
		}
		Long EndingMem = getMemoryUsage();
		return startingMem - EndingMem;
	}

	public static long getMemoryUsage() {
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		return (totalMemory - freeMemory);
	}

	public static long getMemoryFree() {
		return Runtime.getRuntime().freeMemory();
	}

	public static long getMemoryTotal() {
		return Runtime.getRuntime().totalMemory();
	}


	private static void garbageCollect() {
		try {
			System.gc();
			Thread.sleep(100);
			System.runFinalization();
			Thread.sleep(100);
			System.gc();
			Thread.sleep(100);
			System.runFinalization();
			Thread.sleep(100);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}


}
