package ltu.d0007n;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import javax.sql.DataSource;

public class Database {

	public static final String BATCH_SEPARATOR = ";";

	private static final DynamicCreator<DataSource> dsCreator = new DynamicCreator<DataSource>("datasource");

	/**
	 * 
	 */
	private Database() { /* you may not instantiate this object! */ }

    /**
     * Gets a datasource for the named JDBC driver.
	 * <p>
	 * Example driver name: org.apache.derby.jdbc.EmbeddedDataSource
     * <p>
     * Now, these are the naked facts regarding data sources:        
     *
     * There is no uniform way to configure a data source - it is
     * highly proprietary and depends on the JDBC driver.
     *
     * Depending on what data source you are using, you will have to
     * use a construction along the lines of these examples on the
     * returned DataSource.
     * <pre>
     *     DataSource dataSource = Database.getDataSource("org.apache.derby.jdbc.EmbeddedDataSource");
     *     org.apache.derby.jdbc.EmbeddedDataSource ds = (org.apache.derby.jdbc.EmbeddedDataSource)dataSource;
	 *
	 *     // Standard JDBC DataSource configuration (regardless of database)
     *     ds.setDescription("MyApplication");
     *     ds.setDatabaseName("my-database");
     *     ds.setUser("");
     *     ds.setPassword("");
     * 
	 *     // Apache Derby-specific configuration
     *     ds.setCreateDatabase("create");  // Create database if it does not exist
     * </pre>
     * <p>
     * @return DataSource
     * @throws DatabaseException
     */
    public static DataSource getDataSource(String driverName) throws DatabaseException {

        // Dynamically instantiate a DataSource from its name (parameter 'driverName');
        try {
			Class dataSourceClass = dsCreator.createClass(driverName);
			DataSource dataSourceObject = dsCreator.createObject(driverName, dataSourceClass);
            return dataSourceObject;
			
        } catch (ClassNotFoundException cnfe) {
            // Provide context and throw new exception that wraps the old exception.
            // Thus we retain the error information from the original exception, but
            // give context as to what we were trying to achieve when we failed.
            String info = "Could not determine datasource (\"" + driverName + "\"): ";
            info += cnfe.getMessage();
            throw new DatabaseException(info, cnfe);
        }
    }
   
    /**
     * Support for explicit logging of SQL exceptions to error log.
     */
    public static String squeeze(SQLException sqle) {
        SQLException e = sqle;
        StringBuffer buf = new StringBuffer();
        while (e != null) {
            buf.append(sqle.getClass().getSimpleName() + " [");
            buf.append(e.getMessage());
            buf.append("], SQLstate(");
            buf.append(e.getSQLState());
            buf.append("), Vendor code(");
            buf.append(e.getErrorCode());
            buf.append(")");
            e = e.getNextException();
        }
        return buf.toString();
    }

    /**
     * Support for explicit logging of SQL warnings to warning log.
     */
    public static String squeeze(SQLWarning sqlw) {
        SQLWarning w = sqlw;
        StringBuffer buf = new StringBuffer();
        while (w != null) {
            buf.append(sqlw.getClass().getSimpleName() + " [");
            buf.append(w.getMessage());
            buf.append("], SQLstate(");
            buf.append(w.getSQLState());
            buf.append("), Vendor code(");
            buf.append(w.getErrorCode());
            buf.append(")");
            w = w.getNextWarning();
        }
        return buf.toString();
    }

	/**
	 * Reads a text file containing SQL statements, each statement
	 * terminated by a ';' character, and returns them as individual
	 * strings (in a collection).
	 * <p/>
	 * Ignores line comments, i.e. lines starting with "--", 
	 * and also trailing comments on lines.
	 */
    public static Collection<String> readSqlFile(Reader reader) throws IOException {
        Collection<String> sqlStatements = new ArrayList<String>();

        try (BufferedReader in = new BufferedReader(reader)) {

			// prepare first statement
            StringBuilder sqlStatement = new StringBuilder();

			String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();

                // ignore comment lines
                if (line.length() > 0 && !line.startsWith("--")) {
					int pos = 0;
					
                    // ignore trailing comments
                    if ((pos = line.indexOf("--")) >= 0) {
                        line = line.substring(0, pos);
                        line.trim();
                    }

                    // accept batch separator anywhere on line,
                    // considering case
                    //
                    if ((pos = line.indexOf(BATCH_SEPARATOR)) >= 0) {
                        line = line.substring(0, pos);
                        line.trim();

                        // accumulate
                        sqlStatement.append(line).append(" ");

                        // store
                        if (sqlStatement.length() > 0)
                            sqlStatements.add(sqlStatement.toString());

						// prepare next statement
                        sqlStatement = new StringBuilder();
                    } else {
                        // accumulate
                        sqlStatement.append(line).append(" ");
                    }
                }
            }
        }

        return sqlStatements;
    }

    /**
     * Executes an SQL statement
     * <p/>
     * @param sqlStatement - a valid SQL statement
     * @return
     * @throws SQLException
     */
    private static int executeUpdate(DataSource dataSource, String sqlStatement) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                return stmt.executeUpdate(sqlStatement); // auto-commits by default
            }
        }
    }

    /**
     * Loads a file containing SQL code and executes its statements, one at a time,
     * against the database manager (in this case a Java DB / Apache Derby)
     * <p/>
     * @param dataSource - a data source for the database
     * @param is - an input stream for a file containing SQL statements
     * @throws SQLException
     * @throws IOException
     */
    public static void loadAndExecute(DataSource dataSource, InputStream is) throws SQLException, IOException {
        Collection<String> statements;

        Reader reader = new InputStreamReader(is);
        statements = Database.readSqlFile(reader);

        for (String statement : statements) {
            try {
                executeUpdate(dataSource, statement);
            }
            catch (SQLException sqle) {
                // State: 01000 - Generic warning
                //  [SQL Server: Object already exists]
                if (sqle.getSQLState().equals("01000")) {
                    logProblem(sqle, statement, "[OK]: Object already exists - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                // State: 23000 - Integrity constraint/key violation
                //  [SQL Server: Data already exists]
                //  [Oracle:     Data already exists]
                //  [PostgreSQL: Data already exists]
                else if (sqle.getSQLState().startsWith("23")) {
                    logProblem(sqle, statement, "[OK]: Data already exists - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                // State: 42000 - Syntax error or access violation
                //  [Oracle: Object already exists]
                //  [Oracle: Table or view does not exist]
                /*
                 * This happens to match SQL syntax errors on Derby DB, so we
                 * have to remove this check...
                 *
                else if (sqle.getSQLState().startsWith("42")) {
                    logProblem(sqle, statement, "[OK]: Either object already exists or it does not exist - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                */
                // State: 42P07 - Relation "xyz" already exists
                //  [PostgreSQL: Object already exists]
                else if (sqle.getSQLState().equals("42P07")) {
                    logProblem(sqle, statement, "[OK]: Object already exists - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                // State: 72000 - SQL execute phase errors
                // [Oracle: such column list already indexed]
                else if (sqle.getSQLState().startsWith("72")) {
                    logProblem(sqle, statement, "[OK]: An index covering these columns already exists - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                // State: S0002 - Base table not found
                //  [Teradata: Macro 'xyz' does not exist] - when dropping objects
                else if (sqle.getSQLState().equalsIgnoreCase("S0002")) {
                    logProblem(sqle, statement, "[OK]: Object does not exist - ACCEPTED!");
                    continue; // no success - but acceptable
                }
                // State: X0Y32 - <value> '<value>' already exists in <value> '<value>'.
                // State: X0Y68 - <value> '<value>' already exists.
                //  [Derby: Table/View 'XYZ' already exists in Schema 'ZYX'.]
                //  [Derby: PROCEDURE 'XYZ' already exists.]
                else if (sqle.getSQLState().startsWith("X0Y")) {
                    logProblem(sqle, statement, "[OK]: Object already exists - ACCEPTED!");
                    continue; // no success - but acceptable
                }

                //
                // This exception corresponds to something that we do not accept
                //
                logFailure(sqle, statement);
                throw sqle;
            }
        }
    }

    private static void logProblem(SQLException sqle, String statement, String conclusion) {
        System.out.println("------------------------------------------------------------------");
        System.out.println(conclusion);
        System.out.println(Database.squeeze(sqle));
        System.out.println("Statement was: " + statement);
        System.out.println("------------------------------------------------------------------");
        System.out.println();
    }

    private static void logFailure(SQLException sqle, String statement) {
        System.out.println("------------------------------------------------------------------");
        System.out.println(Database.squeeze(sqle));
        System.out.println("[NOT OK]");
        System.out.println("Statement was: " + statement);
        System.out.println("------------------------------------------------------------------");
        System.out.println();
    }
}