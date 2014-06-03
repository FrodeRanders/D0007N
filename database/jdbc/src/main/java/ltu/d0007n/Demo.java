package ltu.d0007n;

import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.sql.DataSource;

public class Demo {
    public static final String APPLICATION_NAME = "Database-access";

    // For simplicity reasons, I choose the same database name as the user name
    public static final String DB_NAME = "d0007n";
    public static final String DB_USER = "d0007n";
    public static final String DB_PASSWORD = "d0007n";

    enum DatabaseBackend {
        AN_EMBEDDED_APACHE_DERBY,
        A_REMOTE_POSTGRESQL
    }

    DatabaseBackend currentBackend = DatabaseBackend.AN_EMBEDDED_APACHE_DERBY;

    private final String GET_STUDENTS_IN_COURSE =
            "SELECT s.name " +
            "FROM courses c, students s, admittances a " +
            "WHERE c.name = ? " +
            " AND  c.id = a.courseid " +
            " AND  s.id = a.studentid " +
            "ORDER BY s.name ASC";

    private final String STORE_STUDENT =
            "INSERT INTO students (id, name) VALUES (?,?)";


    private final DataSource dataSource;
    
    public Demo() throws DatabaseException {
        switch (currentBackend) {
            case A_REMOTE_POSTGRESQL:
                dataSource = Database.getDataSource("org.postgresql.ds.PGPoolingDataSource");
                {
                    // Initiation of parameters for this data source must be done using
                    // the specific class, so we "cast" the general 'DataSource' into a specific
                    // 'PGPoolingDataSource'.
                    //
                    org.postgresql.ds.PGPoolingDataSource ds = (org.postgresql.ds.PGPoolingDataSource) dataSource;
                    ds.setApplicationName(APPLICATION_NAME);
                    ds.setDatabaseName(DB_NAME);
                    ds.setUser(DB_USER);
                    ds.setPassword(DB_PASSWORD);
                    ds.setServerName("localhost");
                    ds.setPortNumber(5432); // default well-known port number
                    ds.setMaxConnections(10);
                }
                break;

            case AN_EMBEDDED_APACHE_DERBY:
            default:
                dataSource = Database.getDataSource("org.apache.derby.jdbc.EmbeddedDataSource");
                {
                    // Initiation of parameters for this data source must be done using
                    // the specific class, so we "cast" the general 'DataSource' into a specific
                    // 'EmbeddedDataSource'.
                    //
                    org.apache.derby.jdbc.EmbeddedDataSource ds = (org.apache.derby.jdbc.EmbeddedDataSource) dataSource;
                    ds.setDescription(APPLICATION_NAME);
                    ds.setDatabaseName(DB_NAME);
                    ds.setUser(DB_USER);  // Not really needed since we are running Derby embedded
                    ds.setPassword(DB_PASSWORD);  // Not really needed since we are running Derby embedded
                    ds.setCreateDatabase("create");  // Create database if it does not exist
                }
                break;
        }
    }

    /**
     * Creates the database objects (tables, etc).
     * <p/>
     * @throws SQLException
     * @throws IOException
     */
    public void create() throws SQLException, IOException {
        try (InputStream is = getClass().getResourceAsStream("create.sql")) {
            Database.loadAndExecute(dataSource, is);
        }
    }

    /**
     * Initiates the database with basic content.
     * <p/>
     * @throws SQLException
     * @throws IOException
     */
    public void initiate() throws SQLException, IOException {
        try (InputStream is = getClass().getResourceAsStream("initiate.sql")) {
            Database.loadAndExecute(dataSource, is);
        }
    }


    /**
     * Gets all students admitted to a specific course
     * <p/>
     */
    public Collection<String> getAllStudents(final String courseName) {
        Collection<String> students = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(GET_STUDENTS_IN_COURSE)) {
                pStmt.setString(1, courseName);

                try (ResultSet rs = pStmt.executeQuery()) {
                    while (rs.next()) {
                        String student = rs.getString("name");
                        students.add(student);
                    }
                }
            }
        }
        catch (SQLException sqle) {
            String info = "Failed to query database: ";
            info += Database.squeeze(sqle);
            System.err.println(info);
        }
        return students;
    }


    /**
     *
     */
    public void main(String[] args) {
        try {
            Demo demo = new Demo();
            demo.create();
            demo.initiate();

            System.out.println("Database ready");

            Collection<String> students = demo.getAllStudents("D0007N");

            System.out.println("--- Students admitted to D0007N ---");
            for (String student : students) {
                System.out.println(student);
            }
            System.out.println();

        } catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();
        }
    }
}
