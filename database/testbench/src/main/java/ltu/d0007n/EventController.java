package ltu.d0007n;

import java.util.ArrayList;
import java.util.Collection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;

public class EventController {
    public static final String APPLICATION_NAME = "Eventmanager"; // since Facebook was already taken :)
    public static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDataSource";
    public static final String DB_NAME = "testbench-db";

    private static final String GET_ALL_EVENTS =
            "SELECT eventid, eventname, eventdate " +
            "FROM events ORDER BY eventdate";

    private static final String GET_ALL_PERSONS =
            "SELECT personid, personname " +
            "FROM persons ORDER BY personid";

    private static final String GET_ALL_REGISTRATIONS =
            "SELECT e.eventid, e.eventname, e.eventdate, p.personid, p.personname " +
            "FROM events e, persons p, registrations r " +
            "WHERE e.eventid = r.eventid " +
            " AND  p.personid = r.personid " +
            "ORDER BY e.eventdate";

    private static final String GET_ALL_REGISTRATIONS_FOR =
            "SELECT e.eventid, e.eventname, e.eventdate, p.personid, p.personname " +
            "FROM events e, persons p, registrations r " +
            "WHERE e.eventid = r.eventid " +
            " AND  p.personid = r.personid " +
            " AND  p.personname = ? " +  /* not using LIKE here, so it has to be exact */
            "ORDER BY e.eventdate";

    private static final String DUMP_HISTORY =
            "SELECT * FROM db_history";


    private final DataSource dataSource;
    
    public EventController() throws DatabaseException {
        dataSource = Database.getDataSource(JDBC_DRIVER);
        org.apache.derby.jdbc.EmbeddedDataSource ds = (org.apache.derby.jdbc.EmbeddedDataSource)dataSource;
     
        // Standard JDBC DataSource configuration (regardless of database)
        ds.setDescription(APPLICATION_NAME);
        ds.setDatabaseName(DB_NAME);
        ds.setUser("");     // Not needed since we are running Derby embedded
        ds.setPassword(""); // Not needed since we are running Derby embedded
      
        // Apache Derby-specific configuration
        ds.setCreateDatabase("create");  // Create database if it does not exist
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
     * Gets all events.
     * <p/>
     */
    public Collection<Event> getAllEvents() throws SQLException {
        Collection<Event> events = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(GET_ALL_EVENTS)) {
                try (ResultSet rs = pStmt.executeQuery()) {
                    while (rs.next()) {
                        Event event = new Event(rs);
                        events.add(event);
                    }
                }
            }
        }
        return events;
    }

    /**
     * Gets all registrations
     */
    public Collection<Registration> getAllRegistrations() throws SQLException {
        Collection<Registration> registrations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(GET_ALL_REGISTRATIONS)) {
                try (ResultSet rs = pStmt.executeQuery()) {
                    while (rs.next()) {
                        Registration registration = new Registration(rs);
                        registrations.add(registration);
                    }
                }
            }
        }
        return registrations;
    }

    /**
     * Gets all persons
     */
    public Collection<Person> getAllPersons() throws SQLException {
        Collection<Person> persons = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(GET_ALL_PERSONS)) {
                try (ResultSet rs = pStmt.executeQuery()) {
                    while (rs.next()) {
                        Person person = new Person(rs);
                        persons.add(person);
                    }
                }
            }
        }
        return persons;
    }


    /**
     * Gets all registrations for named person
     */
    public Collection<Registration> getAllRegistrationsFor(String personName) throws SQLException {
        Collection<Registration> registrations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(GET_ALL_REGISTRATIONS_FOR)) {
                pStmt.setString(1, personName); // matches first '?' in the prepared statement string

                try (ResultSet rs = pStmt.executeQuery()) {
                    while (rs.next()) {
                        Registration registration = new Registration(rs);
                        registrations.add(registration);
                    }
                }
            }
        }
        return registrations;
    }

    /**
     * Dumps the history of this database, as recorded in table DB_HISTORY.
     * <p/>
     * @throws SQLException
     */
    public void dumpDatabaseHistory() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pStmt = conn.prepareStatement(DUMP_HISTORY)) {
                try (ResultSet rs = pStmt.executeQuery()) {
                    System.out.println("Dumping schema history for database");
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("occasion");
                        String description = rs.getString("description");
                        int major = rs.getInt("major");
                        int minor = rs.getInt("minor");
                    
                        System.out.println(ts.toString() + " - " + major + "." + minor + " - " + description);
                    }
                }
            }
        }
    }
}
