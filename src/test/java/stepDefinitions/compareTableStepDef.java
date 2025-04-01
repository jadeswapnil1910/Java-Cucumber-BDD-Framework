package stepDefinitions;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.ini4j.InvalidFileFormatException;
import org.testng.Assert;

import com.swap.manager.ConfigReader;
import com.swap.manager.DBConnection;

import hooks.Hooks;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pageObjects.CompareOracleSqlTable;

public class compareTableStepDef {
	    ConfigReader config = new ConfigReader();
	    Properties prop = config.init_prop("config_query");
	    Properties dbata_prop = config.init_prop("DataConnection");
	    
	    DBConnection data;
	    static Scenario scenario;
	    
	    List<List<Object>> Table1;
	    List<List<Object>> Table2;
	    
	    @Given("Fetch (.*) from (.*) server for table1 (.*).$")
	    public void fetch_rows_from_table1(String RowCol, String database, String table) 
	            throws InvalidFileFormatException, IOException {
	        scenario = Hooks.testCase;
	        
	        data = new DBConnection(database);
	        
	        scenario.log(String.format("[INFO] : Fetching rows from %s.%s ", database, table));
	        
	        String query;
	        
	        if (RowCol.contains("column")) {
	            query = prop.getProperty("col." + table);
	        } else {
	            query = prop.getProperty("query." + table);
	        }
	        
	        Table1 = data.fetch_table_rows(query);
	        
	        scenario.log(String.format("[PASS] : Retrieved %s rows from the %s.%s successfully.", Table1.size(), database, table));
	    }
	    
	    @Given("Fetch (.*) from (.*) server for table2 (.*).$")
	    public void fetch_rows_from_table2(String RowCol, String database, String table) 
	            throws InvalidFileFormatException, IOException {
	    	scenario = Hooks.testCase;
	        
	        data = new DBConnection(database);
	        
	        scenario.log(String.format("[INFO] : Fetching rows from %s.%s ", database, table));
	        
	        String query;
	        
	        if (RowCol.contains("column")) {
	            query = prop.getProperty("col." + table);
	        } else {
	            query = prop.getProperty("query." + table);
	        }
	        
	        Table2 = data.fetch_table_rows(query);
	        
	        scenario.log(String.format("[PASS] : Retrieved rows %s from the %s.%s successfully.", Table2.size(), database, table));
	    }
	    
	    @Then("Compare (.*) between (.*) table1 and (.*) table2")
	    public void compare_data_between_dbil_stg_ora_res_table_and_sql_rufprod_ip_org_res_table(String data, 
	            String table1, String table2) {
	        scenario = Hooks.testCase;
	        
	        scenario.log(String.format("[INFO] : Comparing data between %s and %s table.", table1, table2));
	        
	        boolean dataCompare = false;
	        
	        if (data.equalsIgnoreCase("Column")) {
	            dataCompare = CompareOracleSqlTable.compareColumns(Table1, Table2);
	        } else {
	            dataCompare = CompareOracleSqlTable.compareData(Table1, Table2);
	        }
	        
	        if (dataCompare == true) {
	            scenario.log("[PASS] : Data matched between both Table successfully.");
	        } else {
	            Table1.clear();
	            Table2.clear();
	            Assert.assertTrue(false);
	        }
	        
	        scenario.log("[PASS] : Data matched between both tables successfully.");
	        Table1.clear();
	        Table2.clear();
	    }
	}
