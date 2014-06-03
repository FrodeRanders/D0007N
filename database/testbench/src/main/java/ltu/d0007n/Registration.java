package ltu.d0007n;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class models a registration
 */
public class Registration {
    private final int eventId;
    private final String eventName;
    private final java.sql.Date eventDate;
    private final int personId;
    private final String personName;

    public Registration(ResultSet rs) throws SQLException {
        eventId = rs.getInt("eventid");
        eventName = rs.getString("eventname");
        eventDate = rs.getDate("eventdate");
        personId = rs.getInt("personid");
        personName = rs.getString("personname");
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public java.util.Date getEventDate() {
        return eventDate;
    }

    public int getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Registration<event-id=")
           .append(eventId)
           .append(", event-name=\"")
           .append(eventName)
           .append("\", event-date=")
           .append(eventDate.toLocalDate())
           .append(", person-id=")
           .append(personId)
           .append("\", person-name=\"")
           .append(personName)
           .append("\">");
        return buf.toString();
    }
}
