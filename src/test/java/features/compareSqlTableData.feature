Feature: Validate data consistency between DBIL staging and target tables

@backend @DBIL_STG_RES_AVAIL_vs_TRGT_RES_AVAIL
Scenario Outline: Compare data between <StagingTable> and <TargetTable> tables
    Given Fetch <Data> from <Database1> server for table1 <StagingTable>
    And Fetch <Data> from <Database2> server for table2 <TargetTable>
    Then Compare <Data> between <StagingTable> table1 and <TargetTable> table2

Examples:
    | Data | Database1 | Table1		| Database2  	| Table1   	|
    | data | SQL_DPPM0 | table1 	| SQL_DPPM0 	| table2 		|