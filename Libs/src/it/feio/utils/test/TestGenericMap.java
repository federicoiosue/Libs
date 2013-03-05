package it.feio.utils.test;

import java.util.HashMap;
import it.feio.utils.obj.GenericMap;


public class TestGenericMap {


	public static void main(String[] args) {

//		String regex = "(.+_){3}3_";
//
//		GenericMap m = new GenericMap();
//		m.put("1_X21_2010-05-09 07:58:00.0_4_3", 1);
//		m.put("17_X81_2010-05-07 11:08:00.0_3_3", 2);
//		m.put("46_O10_2010-05-16 09:26:00.0_4_8", 1);
//
//		System.out.println(m);
//		for (String key : m.getKeyRegex(regex)) {
//			m.remove(key);
//		}
//		System.out.println(m);

		/* ***** */
		
		HashMap<Object, Object> m1 = new HashMap<Object, Object>();
		GenericMap m2 = new GenericMap();

		m1.put("val", 7);
		m2.put("val", 7);

		if (Integer.parseInt(m1.get("val").toString()) >= m2.getInt("val")) {
			System.out.println("maggiore");
		}



	}


}
