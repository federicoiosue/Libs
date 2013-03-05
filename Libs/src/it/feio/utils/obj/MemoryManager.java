package it.feio.utils.obj;


public class MemoryManager {


	public static double cleanMemory(long percentage) {
		double freeMemory = (double) Runtime.getRuntime().freeMemory();
		double totalMemory = (double) Runtime.getRuntime().totalMemory();
		if (freeMemory / totalMemory * 100 < percentage) {
			garbageCollect();
			garbageCollect();
		}
		return (totalMemory - freeMemory);
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
