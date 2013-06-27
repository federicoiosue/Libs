package it.feio.utils.db;

import it.feio.utils.file.Replacer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class RsDump {

	public static ArrayList<HashMap<String, String>> dumpToMap(ResultSet rs) throws SQLException {

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		// Estrapolazione dei metadati dei risultati per i nomi delle colonne della tabella
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();

		// Nomi delle colonne
		List<String> fields = new ArrayList<String>();
		for (int i = 1; i < numColumns + 1; i++) {
			fields.add(rsmd.getColumnName(i));
		}

		while (rs.next()) {
			HashMap<String, String> record = new HashMap<String, String>();
			for (String field : fields) {
				record.put(field, rs.getString(field));
			}
			list.add(record);
		}
		// this.dataHeader = dataHeader.replaceFirst(",", "").split(",");

		return list;
	}


	public static void dumpToconsole(ResultSet rs) throws SQLException {
		boolean header = false;
		ArrayList<HashMap<String, String>> list = dumpToMap(rs);
		Map.Entry pairs;
		int length = 0;
		for (HashMap<String, String> record : list) {
			
			if (!header) {
				length = maxLength(record.keySet().iterator());
				Iterator<String> it = record.keySet().iterator();
				System.out.format("|");
				while (it.hasNext()) {
					System.out.format(" %-" + length + "s |", it.next());
				}
				System.out.format("%n");
				String divider = Replacer.repeat("-", length*record.keySet().size());
				System.out.format("|%s|%n", divider);
				header = true;
			}
			Iterator<Entry<String, String>> it = record.entrySet().iterator();
			System.out.format("|");
			while (it.hasNext()) {
				pairs = (Map.Entry) it.next();
				System.out.format(" %-" + length + "s |", pairs.getValue());
			}
			System.out.format("%n");
		}
	}


	private static int maxLength(Iterator<String> it) {
		int maxLength = 0;
		while (it.hasNext()) {
			if (maxLength < it.next().toString().length()) {
				maxLength = it.toString().length();
			}
		}
		return maxLength;
	}



}
