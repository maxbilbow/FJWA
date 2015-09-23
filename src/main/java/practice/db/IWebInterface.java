package practice.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface IWebInterface {
	void openNewConnection() throws SQLException, ClassNotFoundException;
	void closeConnection();

	
	void setValueForKey(String key, Object value);
	void getValue(String key);
	boolean isConnected();
	
	void insertValues(String tableName, String[] columns, String[][] rows) throws SQLException;
	
	Connection getConnection();

	default Statement createStatement() throws SQLException {
		return this.getConnection().createStatement();
	}
	
	
	default IWebInterface withConnection() throws ClassNotFoundException, SQLException {
		this.openNewConnection();
		return this;
	}
	Object getTableData(TableData table) throws FJWAException;
	void printTable(TableData table) throws FJWAException, SQLException, InterruptedException;
	String buildInsertSql(String name, String[] cols, String[][] rows);
	
}
