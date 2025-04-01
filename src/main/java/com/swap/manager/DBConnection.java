package com.swap.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBConnection {

	public List<List<Object>> SqlRows = new ArrayList<>();
	public List<List<Object>> OracleRows = new ArrayList<>();
	final static String dbConnections = "./src/test/resources/dbConnections.ini";
	Wini dbDetails;
	static ConfigReader config = new ConfigReader();
	static Properties prop = config.init_prop("global");
	static String ENV = prop.getProperty("ENV");

	String DriverclassName;
	String Url;
	String Username;
	String Password;
	static JdbcTemplate conn;
	DriverManagerDataSource ds = new DriverManagerDataSource();

	public DBConnection(String database) throws InvalidFileFormatException, IOException {

		this.dbDetails = new Wini(new File(dbConnections));

		if (ENV.equalsIgnoreCase("IST") && database.equalsIgnoreCase("SQL_DPPM0")) {
			this.DriverclassName = dbDetails.get("SQL_DPPM0_IST", "DriverClassName");
			this.Url = dbDetails.get("SQL_DPPMØ_IST", "Url");
			this.Username = dbDetails.get("SQL_DPPMO_IST", "Username");
			this.Password = dbDetails.get("SQL_DPPMØ_IST", "Password");
		} else {
			this.DriverclassName = dbDetails.get(database, "DriverClassName");
			this.Url = dbDetails.get(database, "Url");
			this.Username = dbDetails.get(database, "Username");
			this.Password = dbDetails.get(database, "Password");
		}

		conn = new JdbcTemplate(getDataBaseDetails());
		
		System.out.println("Connected to MS SQL Server");

	}

	private DataSource getDataBaseDetails() {
		ds.setDriverClassName(DriverclassName);
		ds.setUrl(Url);
		ds.setUsername(Username);
		ds.setPassword(Password);
		
		
		return ds;
	}
	
	public List<List<Object>> fetch_table_rows(String SQL_query) {
		
		List<List<Object>> rows = new ArrayList<>();
		
		conn.query(SQL_query, rs -> {
			List<Object> row = new ArrayList<>();
			int columnCount = rs.getMetaData().getColumnCount();
			
			for (int i = 1; i <= columnCount ; i++) {
				row.add(rs.getObject(i));
			}
			rows.add(row);
		});
		return rows;
	}

}
