package com.mangospice.gems.database;

import java.sql.*;
import java.util.Enumeration;

/**
 * Simple util that prints info about a jdbc driver and connects to a jdbc url using that driver.
 * <p/>
 * Examples:
 * <p/>
 * Test with local mysql
 * java -cp .;jdbc\mysql-connector-java-5.1.21.jar  jdbc.JDBCDriverInfo com.mysql.jdbc.Driver "jdbc:mysql://localhost:3306/next?user=root&password=password&useUnicode=true&amp;characterEncoding=UTF8"
 * <p/>
 * Using oracle service name
 * java -cp .;jdbc\ojdbc6.jar JDBCDriverInfo oracle.jdbc.OracleDriver "jdbc:oracle:thin:UNICT/Un1ctest/@//AKLDB001:1521/NZBT"
 * <p/>
 * Using oracle sid
 * java -cp .;jdbc\ojdbc6.jar JDBCDriverInfo oracle.jdbc.OracleDriver "jdbc:oracle:thin:UNICT/Un1ctest@AKLDB001:1521:NZBT"
 *
 * @author oliver.henlich
 */
public class JDBCDriverInfo {
    private void dump(String driverClass) throws Exception {
        loadDriver(driverClass);

        System.out.println("Driver Info:");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver driver = null;
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            if (d.getClass().getName().equals(driverClass)) {
                driver = d;
                break;
            }
        }

        if (driver != null) {
            System.out.println("Driver = " + driver.getClass().getName());
            System.out.println("Major version = " + driver.getMajorVersion());
            System.out.println("Minor version = " + driver.getMinorVersion());
        } else {
            System.err.println("DriverManager could not find driver of type: " + driverClass);
        }

        System.out.println();
    }

    private void dump(String driverClass, String jdbcUrl) throws Exception {
        dump(driverClass);

        Connection connection;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (Exception e) {
            System.err.println("Could not connect to db with url: " + jdbcUrl);
            throw e;
        }


        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            dumpConnectionInfo(databaseMetaData);

            //dumpSpecificInfo(databaseMetaData);

            dumpSimpleInfo(databaseMetaData);

            dumpDatabaseInfo(connection, databaseMetaData);

        } catch (Exception e) {
            System.err.println("Problem reading meta data");
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void dumpConnectionInfo(DatabaseMetaData databaseMetaData) throws SQLException {
        System.out.println("JDBC Connection Info:");
        System.out.println("Product name = " + databaseMetaData.getDatabaseProductName());
        System.out.println("Database Major version = " + databaseMetaData.getDatabaseMajorVersion());
        System.out.println("Database Minor version = " + databaseMetaData.getDatabaseMinorVersion());
        System.out.println("Database Product version = " + databaseMetaData.getDatabaseProductVersion());
        System.out.println("Driver version = " + databaseMetaData.getDriverVersion());
        System.out.println();
    }

    private void dumpSpecificInfo(DatabaseMetaData databaseMetaData) throws SQLException {
        System.out.println("Specific info:");
        System.out.println("supportsResultSetType(TYPE_FORWARD_ONLY) = " + databaseMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
        System.out.println("supportsResultSetType(TYPE_SCROLL_INSENSITIVE) = " + databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
        System.out.println("supportsResultSetType(TYPE_SCROLL_SENSITIVE) = " + databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
        System.out.println();
    }

    private void dumpSimpleInfo(DatabaseMetaData databaseMetaData) throws SQLException {
        System.out.println("Simple info:");
        System.out.println("allProceduresAreCallable = " + databaseMetaData.allProceduresAreCallable());
        System.out.println("allTablesAreSelectable = " + databaseMetaData.allTablesAreSelectable());
        System.out.println("autoCommitFailureClosesAllResultSets = " + databaseMetaData.autoCommitFailureClosesAllResultSets());
        System.out.println("dataDefinitionCausesTransactionCommit = " + databaseMetaData.dataDefinitionCausesTransactionCommit());
        System.out.println("dataDefinitionIgnoredInTransactions = " + databaseMetaData.dataDefinitionIgnoredInTransactions());
        System.out.println("doesMaxRowSizeIncludeBlobs = " + databaseMetaData.doesMaxRowSizeIncludeBlobs());
        System.out.println("getCatalogSeparator = " + databaseMetaData.getCatalogSeparator());
        System.out.println("getCatalogTerm = " + databaseMetaData.getCatalogTerm());
        System.out.println("getDatabaseMajorVersion = " + databaseMetaData.getDatabaseMajorVersion());
        System.out.println("getDatabaseMinorVersion = " + databaseMetaData.getDatabaseMinorVersion());
        System.out.println("getDatabaseProductName = " + databaseMetaData.getDatabaseProductName());
        System.out.println("getDatabaseProductVersion = " + databaseMetaData.getDatabaseProductVersion());
        System.out.println("getDefaultTransactionIsolation = " + databaseMetaData.getDefaultTransactionIsolation());
        System.out.println("getDriverMajorVersion = " + databaseMetaData.getDriverMajorVersion());
        System.out.println("getDriverMinorVersion = " + databaseMetaData.getDriverMinorVersion());
        System.out.println("getDriverName = " + databaseMetaData.getDriverName());
        System.out.println("getDriverVersion = " + databaseMetaData.getDriverVersion());
        System.out.println("getExtraNameCharacters = " + databaseMetaData.getExtraNameCharacters());
        System.out.println("getIdentifierQuoteString = " + databaseMetaData.getIdentifierQuoteString());
        System.out.println("getJDBCMajorVersion = " + databaseMetaData.getJDBCMajorVersion());
        System.out.println("getJDBCMinorVersion = " + databaseMetaData.getJDBCMinorVersion());
        System.out.println("getMaxBinaryLiteralLength = " + databaseMetaData.getMaxBinaryLiteralLength());
        System.out.println("getMaxCatalogNameLength = " + databaseMetaData.getMaxCatalogNameLength());
        System.out.println("getMaxCharLiteralLength = " + databaseMetaData.getMaxCharLiteralLength());
        System.out.println("getMaxColumnNameLength = " + databaseMetaData.getMaxColumnNameLength());
        System.out.println("getMaxColumnsInGroupBy = " + databaseMetaData.getMaxColumnsInGroupBy());
        System.out.println("getMaxColumnsInIndex = " + databaseMetaData.getMaxColumnsInIndex());
        System.out.println("getMaxColumnsInOrderBy = " + databaseMetaData.getMaxColumnsInOrderBy());
        System.out.println("getMaxColumnsInSelect = " + databaseMetaData.getMaxColumnsInSelect());
        System.out.println("getMaxColumnsInTable = " + databaseMetaData.getMaxColumnsInTable());
        System.out.println("getMaxConnections = " + databaseMetaData.getMaxConnections());
        System.out.println("getMaxCursorNameLength = " + databaseMetaData.getMaxCursorNameLength());
        System.out.println("getMaxIndexLength = " + databaseMetaData.getMaxIndexLength());
        System.out.println("getMaxProcedureNameLength = " + databaseMetaData.getMaxProcedureNameLength());
        System.out.println("getMaxRowSize = " + databaseMetaData.getMaxRowSize());
        System.out.println("getMaxSchemaNameLength = " + databaseMetaData.getMaxSchemaNameLength());
        System.out.println("getMaxStatementLength = " + databaseMetaData.getMaxStatementLength());
        System.out.println("getMaxStatements = " + databaseMetaData.getMaxStatements());
        System.out.println("getMaxTableNameLength = " + databaseMetaData.getMaxTableNameLength());
        System.out.println("getMaxTablesInSelect = " + databaseMetaData.getMaxTablesInSelect());
        System.out.println("getMaxUserNameLength = " + databaseMetaData.getMaxUserNameLength());
        System.out.println("getNumericFunctions = " + databaseMetaData.getNumericFunctions());
        System.out.println("getProcedureTerm = " + databaseMetaData.getProcedureTerm());
        System.out.println("getResultSetHoldability = " + databaseMetaData.getResultSetHoldability());
        System.out.println("getRowIdLifetime = " + databaseMetaData.getRowIdLifetime());
        System.out.println("getSchemaTerm = " + databaseMetaData.getSchemaTerm());
        System.out.println("getSearchStringEscape = " + databaseMetaData.getSearchStringEscape());
        System.out.println("getSQLKeywords = " + databaseMetaData.getSQLKeywords());
        System.out.println("getSQLStateType = " + databaseMetaData.getSQLStateType());
        System.out.println("getStringFunctions = " + databaseMetaData.getStringFunctions());
        System.out.println("getSystemFunctions = " + databaseMetaData.getSystemFunctions());
        System.out.println("getTimeDateFunctions = " + databaseMetaData.getTimeDateFunctions());
        System.out.println("getURL = " + databaseMetaData.getURL());
        System.out.println("getUserName = " + databaseMetaData.getUserName());
        System.out.println("isCatalogAtStart = " + databaseMetaData.isCatalogAtStart());
        System.out.println("isReadOnly = " + databaseMetaData.isReadOnly());
        System.out.println("locatorsUpdateCopy = " + databaseMetaData.locatorsUpdateCopy());
        System.out.println("nullPlusNonNullIsNull = " + databaseMetaData.nullPlusNonNullIsNull());
        System.out.println("nullsAreSortedAtEnd = " + databaseMetaData.nullsAreSortedAtEnd());
        System.out.println("nullsAreSortedAtStart = " + databaseMetaData.nullsAreSortedAtStart());
        System.out.println("nullsAreSortedHigh = " + databaseMetaData.nullsAreSortedHigh());
        System.out.println("nullsAreSortedLow = " + databaseMetaData.nullsAreSortedLow());
        System.out.println("storesLowerCaseIdentifiers = " + databaseMetaData.storesLowerCaseIdentifiers());
        System.out.println("storesLowerCaseQuotedIdentifiers = " + databaseMetaData.storesLowerCaseQuotedIdentifiers());
        System.out.println("storesMixedCaseIdentifiers = " + databaseMetaData.storesMixedCaseIdentifiers());
        System.out.println("storesMixedCaseQuotedIdentifiers = " + databaseMetaData.storesMixedCaseQuotedIdentifiers());
        System.out.println("storesUpperCaseIdentifiers = " + databaseMetaData.storesUpperCaseIdentifiers());
        System.out.println("storesUpperCaseQuotedIdentifiers = " + databaseMetaData.storesUpperCaseQuotedIdentifiers());
        System.out.println("supportsAlterTableWithAddColumn = " + databaseMetaData.supportsAlterTableWithAddColumn());
        System.out.println("supportsAlterTableWithDropColumn = " + databaseMetaData.supportsAlterTableWithDropColumn());
        System.out.println("supportsANSI92EntryLevelSQL = " + databaseMetaData.supportsANSI92EntryLevelSQL());
        System.out.println("supportsANSI92FullSQL = " + databaseMetaData.supportsANSI92FullSQL());
        System.out.println("supportsANSI92IntermediateSQL = " + databaseMetaData.supportsANSI92IntermediateSQL());
        System.out.println("supportsBatchUpdates = " + databaseMetaData.supportsBatchUpdates());
        System.out.println("supportsCatalogsInDataManipulation = " + databaseMetaData.supportsCatalogsInDataManipulation());
        System.out.println("supportsCatalogsInIndexDefinitions = " + databaseMetaData.supportsCatalogsInIndexDefinitions());
        System.out.println("supportsCatalogsInPrivilegeDefinitions = " + databaseMetaData.supportsCatalogsInPrivilegeDefinitions());
        System.out.println("supportsCatalogsInProcedureCalls = " + databaseMetaData.supportsCatalogsInProcedureCalls());
        System.out.println("supportsCatalogsInTableDefinitions = " + databaseMetaData.supportsCatalogsInTableDefinitions());
        System.out.println("supportsColumnAliasing = " + databaseMetaData.supportsColumnAliasing());
        System.out.println("supportsConvert = " + databaseMetaData.supportsConvert());
        System.out.println("supportsCoreSQLGrammar = " + databaseMetaData.supportsCoreSQLGrammar());
        System.out.println("supportsCorrelatedSubqueries = " + databaseMetaData.supportsCorrelatedSubqueries());
        System.out.println("supportsDataDefinitionAndDataManipulationTransactions = " + databaseMetaData.supportsDataDefinitionAndDataManipulationTransactions());
        System.out.println("supportsDataManipulationTransactionsOnly = " + databaseMetaData.supportsDataManipulationTransactionsOnly());
        System.out.println("supportsDifferentTableCorrelationNames = " + databaseMetaData.supportsDifferentTableCorrelationNames());
        System.out.println("supportsExpressionsInOrderBy = " + databaseMetaData.supportsExpressionsInOrderBy());
        System.out.println("supportsExtendedSQLGrammar = " + databaseMetaData.supportsExtendedSQLGrammar());
        System.out.println("supportsFullOuterJoins = " + databaseMetaData.supportsFullOuterJoins());
        System.out.println("supportsGetGeneratedKeys = " + databaseMetaData.supportsGetGeneratedKeys());
        System.out.println("supportsGroupBy = " + databaseMetaData.supportsGroupBy());
        System.out.println("supportsGroupByBeyondSelect = " + databaseMetaData.supportsGroupByBeyondSelect());
        System.out.println("supportsGroupByUnrelated = " + databaseMetaData.supportsGroupByUnrelated());
        System.out.println("supportsIntegrityEnhancementFacility = " + databaseMetaData.supportsIntegrityEnhancementFacility());
        System.out.println("supportsLikeEscapeClause = " + databaseMetaData.supportsLikeEscapeClause());
        System.out.println("supportsLimitedOuterJoins = " + databaseMetaData.supportsLimitedOuterJoins());
        System.out.println("supportsMinimumSQLGrammar = " + databaseMetaData.supportsMinimumSQLGrammar());
        System.out.println("supportsMixedCaseIdentifiers = " + databaseMetaData.supportsMixedCaseIdentifiers());
        System.out.println("supportsMixedCaseQuotedIdentifiers = " + databaseMetaData.supportsMixedCaseQuotedIdentifiers());
        System.out.println("supportsMultipleOpenResults = " + databaseMetaData.supportsMultipleOpenResults());
        System.out.println("supportsMultipleResultSets = " + databaseMetaData.supportsMultipleResultSets());
        System.out.println("supportsMultipleTransactions = " + databaseMetaData.supportsMultipleTransactions());
        System.out.println("supportsNamedParameters = " + databaseMetaData.supportsNamedParameters());
        System.out.println("supportsNonNullableColumns = " + databaseMetaData.supportsNonNullableColumns());
        System.out.println("supportsOpenCursorsAcrossCommit = " + databaseMetaData.supportsOpenCursorsAcrossCommit());
        System.out.println("supportsOpenCursorsAcrossRollback = " + databaseMetaData.supportsOpenCursorsAcrossRollback());
        System.out.println("supportsOpenStatementsAcrossCommit = " + databaseMetaData.supportsOpenStatementsAcrossCommit());
        System.out.println("supportsOpenStatementsAcrossRollback = " + databaseMetaData.supportsOpenStatementsAcrossRollback());
        System.out.println("supportsOrderByUnrelated = " + databaseMetaData.supportsOrderByUnrelated());
        System.out.println("supportsOuterJoins = " + databaseMetaData.supportsOuterJoins());
        System.out.println("supportsPositionedDelete = " + databaseMetaData.supportsPositionedDelete());
        System.out.println("supportsPositionedUpdate = " + databaseMetaData.supportsPositionedUpdate());
        System.out.println("supportsSavepoints = " + databaseMetaData.supportsSavepoints());
        System.out.println("supportsSchemasInDataManipulation = " + databaseMetaData.supportsSchemasInDataManipulation());
        System.out.println("supportsSchemasInIndexDefinitions = " + databaseMetaData.supportsSchemasInIndexDefinitions());
        System.out.println("supportsSchemasInPrivilegeDefinitions = " + databaseMetaData.supportsSchemasInPrivilegeDefinitions());
        System.out.println("supportsSchemasInProcedureCalls = " + databaseMetaData.supportsSchemasInProcedureCalls());
        System.out.println("supportsSchemasInTableDefinitions = " + databaseMetaData.supportsSchemasInTableDefinitions());
        System.out.println("supportsSelectForUpdate = " + databaseMetaData.supportsSelectForUpdate());
        System.out.println("supportsStatementPooling = " + databaseMetaData.supportsStatementPooling());
        System.out.println("supportsStoredFunctionsUsingCallSyntax = " + databaseMetaData.supportsStoredFunctionsUsingCallSyntax());
        System.out.println("supportsStoredProcedures = " + databaseMetaData.supportsStoredProcedures());
        System.out.println("supportsSubqueriesInComparisons = " + databaseMetaData.supportsSubqueriesInComparisons());
        System.out.println("supportsSubqueriesInExists = " + databaseMetaData.supportsSubqueriesInExists());
        System.out.println("supportsSubqueriesInIns = " + databaseMetaData.supportsSubqueriesInIns());
        System.out.println("supportsSubqueriesInQuantifieds = " + databaseMetaData.supportsSubqueriesInQuantifieds());
        System.out.println("supportsTableCorrelationNames = " + databaseMetaData.supportsTableCorrelationNames());
        System.out.println("supportsTransactions = " + databaseMetaData.supportsTransactions());
        System.out.println("supportsUnion = " + databaseMetaData.supportsUnion());
        System.out.println("supportsUnionAll = " + databaseMetaData.supportsUnionAll());
        System.out.println("usesLocalFilePerTable = " + databaseMetaData.usesLocalFilePerTable());
        System.out.println("usesLocalFiles = " + databaseMetaData.usesLocalFiles());
        System.out.println();
    }

    private void dumpDatabaseInfo(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        System.out.println("Database Info:");

        System.out.println("Schemas");
        ResultSet schemas = databaseMetaData.getSchemas();
        printResultSet(schemas);
        System.out.println();

        System.out.println("Catalogs");
        ResultSet catalogs = databaseMetaData.getCatalogs();
        printResultSet(catalogs);
        System.out.println();

        if (connection.getCatalog() != null) {
            System.out.println("Tables for current Catalog (" + connection.getCatalog() + ")");
            printTables(connection, databaseMetaData);

            System.out.println("Procedures for current Catalog (" + connection.getCatalog() + ")");
            printProcedures(connection, databaseMetaData);
        } else {
            System.out.println("No catalogues. Using schema only.");
        }
    }

    private void printProcedures(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet procedures = databaseMetaData.getProcedures(connection.getCatalog(), null, null);
        while (procedures.next()) {
            System.out.print(" - ");
            System.out.print(procedures.getString("PROCEDURE_NAME"));
            System.out.println();
        }
        System.out.println();
    }

    private void printTables(Connection connection, DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet tables = databaseMetaData.getTables(connection.getCatalog(), null, null, null);
        while (tables.next()) {
            System.out.print(" - ");
            System.out.print(tables.getString("TABLE_NAME"));
            System.out.println();
        }
        System.out.println();
    }


    private void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData md = resultSet.getMetaData();
        int numberOfColumns = md.getColumnCount();

        while (resultSet.next()) {
            System.out.print(" - ");
            for (int i = 1; i <= numberOfColumns; i++) {
                String columnName = md.getColumnName(i);
                System.out.print(resultSet.getString(columnName));
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    private void loadDriver(String driverClass) throws Exception {
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            System.err.println("Problem loading driver: " + driverClass);
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args != null && args.length > 0) {
            JDBCDriverInfo info = new JDBCDriverInfo();
            if (args.length == 1) {
                info.dump(args[0]);
            } else if (args.length == 2) {
                info.dump(args[0], args[1]);
            }
        } else {
            System.err.println("java com.mangospice.gems.database.JDBCDriverInfo <driver class name> [jdbc url]");
        }
    }
}