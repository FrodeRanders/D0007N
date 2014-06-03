package ltu.d0007n;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class models an event and corresponds to the relation 'events' in the database
 */
public class Event {
    private final int eventId;
    private final String eventName;
    private final java.sql.Date eventDate;

    public Event(int id, String name, java.sql.Date date) {
        eventId = id;
        eventName = name;
        eventDate = date;
    }

    public Event(ResultSet rs) throws SQLException {
        eventId = rs.getInt("eventid");
        eventName = rs.getString("eventname");
        eventDate = rs.getDate("eventdate");
    }

    public int getId() {
        return eventId;
    }

    public String getName() {
        return eventName;
    }

    public java.util.Date getDate() {
        return eventDate;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Event<event-id=")
           .append(eventId)
           .append(", event-name=\"")
           .append(eventName)
           .append("\", event-date=")
           .append(eventDate.toLocalDate())
           .append(">");
        return buf.toString();
    }
}
