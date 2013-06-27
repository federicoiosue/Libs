package it.feio.utils.obj;

import java.util.LinkedHashMap;

public class MapSplitter extends Object {

	private static String objectSeparator;
	private static String keyValueSeparator;

	public static MapSplitter on(String objectSeparator) {
		MapSplitter.objectSeparator = objectSeparator;
		return null;
	}

	public static MapSplitter withKeyValueSeparator(String keyValueSeparator) {
		MapSplitter.keyValueSeparator = keyValueSeparator;
		return null;
	}

	public static LinkedHashMap<String, String> split(CharSequence s) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		String[] a = s.toString().split(objectSeparator);

		for (int i = 0; i < a.length ; i++) {
			String[] array = a[i].split(keyValueSeparator);
			map.put(array[0].trim(), array[1].trim());
		}
		return map;
	}


}