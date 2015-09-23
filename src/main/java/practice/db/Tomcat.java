package practice.db;

import java.sql.Connection;
import java.sql.SQLException;

public class Tomcat implements IWebInterface {

	@Override
	public void openNewConnection() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueForKey(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getValue(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insertValues(String tableName, String[] columns, String[][] rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTableData(TableData table) throws FJWAException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printTable(TableData table) throws FJWAException, SQLException, InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String buildInsertSql(String name, String[] cols, String[][] rows) {
		// TODO Auto-generated method stub
		return null;
	}

}
