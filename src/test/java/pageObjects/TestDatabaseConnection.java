package pageObjects;

import java.io.IOException;
//import java.util.Iterator;
import java.util.List;

import org.ini4j.InvalidFileFormatException;

import com.swap.manager.DBConnection;

public class TestDatabaseConnection {

	public static void main(String[] args) throws InvalidFileFormatException, IOException {

		DBConnection db = new DBConnection("SWAPNIL_DB");

		List<List<Object>> fetch_table_rows = db.fetch_table_rows("SELECT * FROM Customer WHERE City = 'London' ORDER BY Id");
		
		for (List<Object> list : fetch_table_rows) {
			System.out.println(list);
		}
	}

}
