package it.feio.utils.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SimpleInsertQueryConstructor {
	
	private String table;
	private List<String> columns, values;
	private String query;
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public SimpleInsertQueryConstructor() {
		columns = new ArrayList<String>();
		values = new ArrayList<String>();
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	protected void addColumn(String columnName) {
		columns.add(columnName);
	}
	
	protected void addNumber(Number num) {
		if(num==null)
			values.add("null");
		else
			values.add(num.toString());
	}
	
	public void addNumber(String column, Number num) {
		this.addColumn(column);
		this.addNumber(num);
	}
	
	protected void addDate(Date date) {
		if(date==null)
			values.add("null");
		else {
			String value = "TO_DATE('"+sdfDate.format(date)+"','dd-mm-yyyy hh24:mi:ss')";
			values.add(value);
		}
	}
	
	public void addDate(String column,Date date) {
		this.addColumn(column);
		this.addDate(date);
	}
	
	protected void addText(CharSequence text) {
		if(text==null) {
			values.add("null");
		}else {
			String value = text.toString().replace("'","''");
			values.add("'"+value+"'");
		}
	}
	
	public void addText(String column, CharSequence text) {
		this.addColumn(column);
		this.addText(text);
	}
	
	protected void addChar(Character ch) {
		if(ch==null)
			values.add("null");
		else {
			if(ch=='\'')
				values.add("''''");
			else
				values.add("'"+ch+"'");
		}
	}
	
	public void addChar(String column, Character ch) {
		this.addColumn(column);
		this.addChar(ch);
	}
	
	public void buildQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ").append(this.getTable()).append("\n");
		if(!columns.isEmpty()) {
			query.append("\t(");
			Iterator<String> it = columns.iterator();
			while (it.hasNext()) {
				String col = (String) it.next();
				query.append(col);
				if(it.hasNext())
					query.append(",");
			}
			query.append(")\n");
		}
		query.append("VALUES \n\t(");
		Iterator<String> itV = values.iterator();
		while (itV.hasNext()) {
			String value = (String) itV.next();
			query.append(value);
			if(itV.hasNext())
				query.append(",");
		}
		query.append(")\n");
		this.query = query.toString();
	}
	
	public String getQuery() {
		if(query==null)
			buildQuery();
		return query;
	}
	
}
