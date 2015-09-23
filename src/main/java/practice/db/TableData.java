package practice.db;

public enum TableData {
	contact_information("contact_information",""
			+ " id int PRIMARY KEY,"
			+ " name varchar(255), "
			+ " email varchar(255) UNIQUE"
			),
	
	test_table("test_table",""
			+ " id int PRIMARY KEY,"
			+ " name varchar(255), "
			+ " email varchar(255) UNIQUE"
			);
	
	private final String name, creationSrc;
	
	private TableData(String name, String creationSrc) {
		this.name = name;
		this.creationSrc = creationSrc;
	}
	
	public String toString() {
		return name;
	}
	
	public String createTableSrc() {
		return "CREATE TABLE " + name +  " ("
				+ creationSrc
				+ ");";
	}
	
	public String fetchTable() {
		return "SELECT * FROM " + name;
	}
	
}
