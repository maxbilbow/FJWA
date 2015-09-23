/**
 * 
 */
package practice.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * @author bilbowm
 *
 */
public class WebInterface implements IWebInterface {

	
	private Connection connection;
	private static String url = "jdbc:sqlite:fjwadb.sql", user = "max", password = "purple";
	
	public WebInterface() throws ClassNotFoundException, SQLException {
		this.openNewConnection();
	}
	
	@Override
	public void openNewConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");  
		connection = DriverManager.getConnection(url, user, password);
	}

	@Override
	public void closeConnection() {
		try {
			if (!connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static final String
	TableWasCreated = "Table Was Created",
	TableWasNotCreated = "Table Was Not Created",
	SuccessfullyConnectedToTable = "Successfully Connected To Table";
	
	@Override
	public Object getTableData(TableData table) throws FJWAException {
		try {
			PreparedStatement statement = connection.prepareStatement(table.fetchTable());
			return statement.executeQuery();
		} catch (SQLException e) {
			try {
				Statement s = connection.createStatement();
				s.execute(table.createTableSrc());
				return TableWasCreated;
			} catch (SQLException e1) {
				throw new FJWAException(e1,"Failed to create tabe");
			}
		}
	}

	@Override
	public void setValueForKey(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getValue(String key) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	public boolean isConnected() {
		try {
			if (!connection.isClosed())
				return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	
	public String buildInsertSql(String table, String[] columns, String[][] rows) {
		String sql = "INSERT INTO " + table + "( ";
		for (String field : columns) {
			sql += field + ", ";
		}
		sql = sql.subSequence(0, sql.length()-2) + " ) VALUES ";
		for (String[] row : rows) {
			sql += "( '";
			for (String cell : row) {
				sql += cell + "', '";
			}
			sql = sql.subSequence(0, sql.length()-3) + "), (";
		}
		sql = sql.subSequence(0, sql.length()-3) + ";";
		return sql;
	}

	@Override
	public void insertValues(String table, String[] columns, String[][] rows) throws SQLException {
		String sql = this.buildInsertSql(table, columns, rows);
		this.createStatement().execute(sql);
		
	}

	@Override
	public void printTable(TableData table) throws FJWAException, SQLException {
		Object data = this.getTableData(table);
		if (ResultSet.class.isInstance(data)) {
			ResultSet res = (ResultSet) data;
			while (res.next()) {
			for (int col = 1; col <= res.getMetaData().getColumnCount(); ++col)
				System.out.print(res.getString(col) + ", ");
			System.out.println();
			} 
			
			res.close();	
		}
	
	}

	
	

}
