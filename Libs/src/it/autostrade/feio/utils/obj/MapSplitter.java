package it.autostrade.feio.utils.obj;

import java.util.LinkedHashMap;
import java.util.Map;

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

	public static Map<String, String> split(CharSequence s) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		String[] a = s.toString().split(objectSeparator);

		for (int i = 0; i < a.length ; i++) {
			String[] array = a[i].split(keyValueSeparator);
			map.put(array[0], array[1]);
		}
		return map;
	}


}