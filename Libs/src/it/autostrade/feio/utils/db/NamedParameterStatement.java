package it.autostrade.feio.utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Classe wrapper per {@link PreparedStatement} per utilizzare variabili parametriche nelle query con un 
 * nome (come fa Oracle) invece che con un indice.
 * 
 * Permette di sostituire una scrittura di questo tipo:
 * 		String query="select * from tabella where name=? or address=?"; 
 * 		PreparedStatement ps = conn.prepareStatement(query); 
 * 		ps.setString(1, "tizio"); 
 * 		p.setString(2, "via attilio 12"); 
 * 		ResultSet rs=p.executeQuery(); 
 * con una di quest'altro, molto più leggibile:
 *		String query="select * from tabella where name=:name or address=:address"; 
 *		NamedParameterStatement nps = new NamedParameterStatement(conn, query); 
 *		nps.setString("name", "tizio"); 
 *		nps.setString("address", "via attilio 12"); 
 *		ResultSet rs = nps.executeQuery();
 * 
 * @author 17000026 (Federico Iosue - Sistemi&Servizi) 28/dic/2012
 */

public class NamedParameterStatement {

	/** The statement this object is wrapping. */
	private final PreparedStatement statement;

	/** Maps parameter names to arrays of ints which are the parameter indices. */
	private final Map indexMap;

	/** Original query before variable parsing */
	private String originalQuery;


	/**
	 * Creates a NamedParameterStatement. Wraps a call to c.{@link Connection#prepareStatement(java.lang.String) prepareStatement}.
	 * 
	 * @param connection
	 *              the database connection
	 * @param query
	 *              the parameterized query
	 * @throws SQLException
	 *               if the statement could not be created
	 */
	public NamedParameterStatement(Connection connection, String query) throws SQLException {
		indexMap = new HashMap();
		originalQuery = query;
		String parsedQuery = parse(query, indexMap);
		statement = connection.prepareStatement(parsedQuery);
	}


	/**
	 * Parses a query with named parameters. The parameter-index mappings are put into the map, and the parsed query is returned. DO NOT CALL FROM CLIENT CODE. This method is non-private so JUnit code can test it.
	 * 
	 * @param query
	 *              query to parse
	 * @param paramMap
	 *              map to hold parameter-index mappings
	 * @return the parsed query
	 */
	static final String parse(String query, Map<String, List> paramMap) {
		// I was originally using regular expressions, but they didn't work well for ignoring
		// parameter-like strings inside quotes.
		int length = query.length();
		StringBuffer parsedQuery = new StringBuffer(length);
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		int index = 1;

		for (int i = 0; i < length; i++) {
			char c = query.charAt(i);
			if (inSingleQuote) {
				if (c == '\'') {
					inSingleQuote = false;
				}
			} else if (inDoubleQuote) {
				if (c == '"') {
					inDoubleQuote = false;
				}
			} else {
				if (c == '\'') {
					inSingleQuote = true;
				} else if (c == '"') {
					inDoubleQuote = true;
				} else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
					int j = i + 2;
					while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
						j++;
					}
					String name = query.substring(i + 1, j);
					c = '?'; // replace the parameter with a question mark
					i += name.length(); // skip past the end if the parameter

					List indexList = (List) paramMap.get(name);
					if (indexList == null) {
						indexList = new LinkedList<Integer>();
						paramMap.put(name, indexList);
					}
					indexList.add(new Integer(index));

					index++;
				}
			}
			parsedQuery.append(c);
		}

		// replace the lists of Integer objects with arrays of ints
		for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Map.Entry) itr.next();
			List list = (List) entry.getValue();
			int[] indexes = new int[list.size()];
			int i = 0;
			for (Iterator itr2 = list.iterator(); itr2.hasNext();) {
				Integer x = (Integer) itr2.next();
				indexes[i++] = x.intValue();
			}
			entry.setValue(indexes);
		}

		return parsedQuery.toString();
	}


	/**
	 * Returns the indexes for a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @return parameter indexes
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 */
	private int[] getIndexes(String name) {
		int[] indexes = (int[]) indexMap.get(name);
		if (indexes == null) {
			throw new IllegalArgumentException("Parameter not found: " + name);
		}
		return indexes;
	}


	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @param value
	 *              parameter value
	 * @throws SQLException
	 *               if an error occurred
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 * @see PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(String name, Object value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			statement.setObject(indexes[i], value);
		}
		indexMap.put(name, value);
	}


	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @param value
	 *              parameter value
	 * @throws SQLException
	 *               if an error occurred
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 * @see PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(String name, String value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			statement.setString(indexes[i], value);
		}
		indexMap.put(name, "'" + value + "'");
	}


	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @param value
	 *              parameter value
	 * @throws SQLException
	 *               if an error occurred
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setInt(String name, int value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			statement.setInt(indexes[i], value);
		}
		indexMap.put(name, value);
	}


	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @param value
	 *              parameter value
	 * @throws SQLException
	 *               if an error occurred
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 * @see PreparedStatement#setInt(int, int)
	 */
	public void setLong(String name, long value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			statement.setLong(indexes[i], value);
		}
		indexMap.put(name, value);
	}


	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *              parameter name
	 * @param value
	 *              parameter value
	 * @throws SQLException
	 *               if an error occurred
	 * @throws IllegalArgumentException
	 *               if the parameter does not exist
	 * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(String name, Timestamp value) throws SQLException {
		int[] indexes = getIndexes(name);
		for (int i = 0; i < indexes.length; i++) {
			statement.setTimestamp(indexes[i], value);
		}
		indexMap.put(name, value);
	}


	/**
	 * Returns the underlying statement.
	 * 
	 * @return the statement
	 */
	public PreparedStatement getStatement() {
		return statement;
	}


	/**
	 * Executes the statement.
	 * 
	 * @return true if the first result is a {@link ResultSet}
	 * @throws SQLException
	 *               if an error occurred
	 * @see PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return statement.execute();
	}


	/**
	 * Executes the statement, which must be a query.
	 * 
	 * @return the query results
	 * @throws SQLException
	 *               if an error occurred
	 * @see PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		return statement.executeQuery();
	}


	/**
	 * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE statement; or an SQL statement that returns nothing, such as a DDL statement.
	 * 
	 * @return number of rows affected
	 * @throws SQLException
	 *               if an error occurred
	 * @see PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return statement.executeUpdate();
	}


	/**
	 * Closes the statement.
	 * 
	 * @throws SQLException
	 *               if an error occurred
	 * @see Statement#close()
	 */
	public void close() throws SQLException {
		statement.close();
	}


	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws SQLException
	 *               if something went wrong
	 */
	public void addBatch() throws SQLException {
		statement.addBatch();
	}


	/**
	 * Executes all of the batched statements.
	 * 
	 * See {@link Statement#executeBatch()} for details.
	 * 
	 * @return update counts for each statement
	 * @throws SQLException
	 *               if something went wrong
	 */
	public int[] executeBatch() throws SQLException {
		return statement.executeBatch();
	}
	
	
	public String dump() {
		String query = null;
		Iterator it = indexMap.entrySet().iterator();
		Map.Entry pairs;
		while (it.hasNext()) {
			pairs = (Map.Entry) it.next();
			query = originalQuery.replace(":" + pairs.getKey().toString(), pairs.getValue().toString());
		}
//		return indexMap.toString();
		return query;
	}
	
	
}
