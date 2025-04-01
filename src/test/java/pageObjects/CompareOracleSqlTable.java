package pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to compare data between two tables represented as List<List<Object>>.
 * Provides methods to compare column structure, row data, and generate mismatch reports.
 */
public class CompareOracleSqlTable {

    // List to store differences between the two tables
    private static final List<String> differences = new ArrayList<>();

    // Table names for logging purposes
    private static String table1Name;
    private static String table2Name;

    /**
     * Compares the column structure of two tables.
     *
     * @param oracleColumns List of columns from the Oracle table.
     * @param sqlColumns    List of columns from the SQL table.
     * @return true if the column structures match, false otherwise.
     */
    public static boolean compareColumns(List<List<Object>> oracleColumns, List<List<Object>> sqlColumns) {
        System.out.println("[INFO] : No. of columns in SQL view : " + sqlColumns.size());
        System.out.println("[INFO] : No. of columns in Oracle view : " + oracleColumns.size());

        if (sqlColumns.size() != oracleColumns.size()) {
            System.err.println("[FAIL] : Column count does not match.");
            return false;
        }

        System.out.println("[PASS] : Column count matches between both views.");

        for (int i = 0; i < sqlColumns.size(); i++) {
            List<Object> sqlColumn = sqlColumns.get(i);
            List<Object> oracleColumn = oracleColumns.get(i);

            for (int j = 0; j < sqlColumn.size(); j++) {
                try {
                    if (!sqlColumn.get(j).toString().trim().equalsIgnoreCase(oracleColumn.get(j).toString().trim())) {
                        differences.add(String.format("Row: %d, Column: %d, SQL: |%s|, Oracle: |%s|",
                                i + 1, j + 1, sqlColumn.get(j), oracleColumn.get(j)));
                    }
                } catch (Exception e) {
                    System.err.println(String.format("Error comparing Row: %d, Column: %d", i + 1, j + 1));
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (!differences.isEmpty()) {
            System.err.println("[FAIL] : Column names do not match between both tables.");
            logDifferences();
            return false;
        }

        return true;
    }

    /**
     * Compares the data rows of two tables.
     *
     * @param table1Rows List of rows from the first table.
     * @param table2Rows List of rows from the second table.
     * @return true if the data matches, false otherwise.
     */
    public static boolean compareData(List<List<Object>> table1Rows, List<List<Object>> table2Rows) {
        System.out.println("[INFO] : No. of rows in Table 1: " + table1Rows.size());
        System.out.println("[INFO] : No. of rows in Table 2: " + table2Rows.size());

        for (int i = 0; i < table1Rows.size(); i++) {
            List<Object> row1 = table1Rows.get(i);
            List<Object> row2 = table2Rows.get(i);

            for (int j = 0; j < row1.size(); j++) {
                try {
                    String value1 = normalizeValue(row1.get(j));
                    String value2 = normalizeValue(row2.get(j));

                    if (!value1.equals(value2)) {
                        differences.add(String.format("Row: %d, Column: %d, Table1: |%s|, Table2: |%s|",
                                i + 1, j + 1, value1, value2));
                    }
                } catch (Exception e) {
                    System.err.println(String.format("Error comparing Row: %d, Column: %d", i + 1, j + 1));
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (!differences.isEmpty()) {
            System.err.println("[FAIL] : Data does not match between the two tables.");
            logDifferences();
            return false;
        }

        System.out.println("[PASS] : Data matches between the two tables.");
        return true;
    }

    /**
     * Normalizes a value by handling nulls and trimming whitespace.
     *
     * @param value The value to normalize.
     * @return A normalized string representation of the value.
     */
    private static String normalizeValue(Object value) {
        if (value == null) {
            return "null";
        }
        String normalized = value.toString().replaceAll("(\r\n|\n)", "").trim();
        normalized = normalized.replace("\n", "").replace("\r\n", "").replace("bsp;", " ").replace("@", " ");
        return normalized;
    }

    /**
     * Logs the differences to the console and clears the differences list.
     */
    private static void logDifferences() {
        System.out.println("Mismatched data count: " + differences.size());
        differences.stream().limit(40).forEach(System.out::println);
        differences.clear();
    }

    /**
     * Writes mismatched data to a file for further analysis.
     *
     * @param mismatchedData List of mismatched data entries.
     */
    public static void writeMismatchReport(List<String> mismatchedData) {
        String timestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String fileName = String.format("..\\test-output\\CompareReport\\Compare_%s_TO_%s_%s.txt", 
                          table1Name, table2Name, timestamp);
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Mismatched data count: " + mismatchedData.size() + System.lineSeparator());
            for (String data : mismatchedData) {
                writer.write(data + System.lineSeparator());
            }
            System.out.println("Mismatch report written to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing mismatch report.");
            e.printStackTrace();
        }
    }
}