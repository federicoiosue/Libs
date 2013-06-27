package it.feio.utils.db;

import it.autostrade.bmt.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleSelectQueryConstructor {
	
	protected List<Pair<String,String>> select, from;
	protected String whereClause, orderByClause, groupByClause;
	protected String query;
	
	public SimpleSelectQueryConstructor() {
		init();
	}
	
	private void init() {
		select = new ArrayList<Pair<String,String>>();
		from = new ArrayList<Pair<String,String>>();
		whereClause = null;
		query = null;
		orderByClause = null;
		groupByClause = null;
	}
	
	public void reset() {
		init();
	}
	
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
		
	public String getWhereClause() {
		return whereClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	public SimpleSelectQueryConstructor addSelectElement(String element) {
		return addSelectElement(element, null);
	}
	
	public SimpleSelectQueryConstructor addSelectElement(String element, String alias) {
		if(alias == null)
			alias = "";
		select.add(new Pair<String, String>(element,alias));
		return this;
	}
	
	public SimpleSelectQueryConstructor addFromElement(String element) {
		return addFromElement(element, null);
	}
	
	public SimpleSelectQueryConstructor addFromElement(String element, String alias) {
		if(alias == null)
			alias = "";
		from.add(new Pair<String, String>(element,alias));
		return this;
	}
	
	public void buildQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		Iterator<Pair<String, String>> it = select.iterator();
		
		while(it.hasNext()) {
			Pair<String, String> pair = it.next();
			String select = pair.getVal1();
			String alias = pair.getVal2();
			query.append(select);
			if(alias!=null && !alias.equals(""))
				query.append(" AS ").append(alias);
			if(it.hasNext())
				query.append(", ");
		}
		query.append("\n FROM \n");
		it = from.iterator();
		while(it.hasNext()) {
			Pair<String, String> pair = it.next();
			String from = pair.getVal1();
			String alias = pair.getVal2();
			query.append("(").append(from).append(")");
			query.append(" ").append(alias);
			if(it.hasNext())
				query.append(", ");
		}
		
		if(whereClause!=null && !whereClause.equals("")) {
			query.append("\n WHERE ");
			query.append(whereClause);
		}
		
		if(groupByClause!=null && !groupByClause.equals("")) {
			query.append("\n GROUP BY ");
			query.append(groupByClause);
		}
		
		if(orderByClause!=null && !orderByClause.equals("")) {
			query.append("\nORDER BY ");
			query.append(orderByClause);
		}
		
//		query.append(";");
		this.query = query.toString();
	}
	
	public String getQuery() {
		if(this.query == null) {
			buildQuery();
		}
		return this.query;
	}

	public void setGroupByClause(String groupByClause) {
		this.groupByClause = groupByClause;
	}

}
