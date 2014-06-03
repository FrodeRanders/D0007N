package ltu.d0007n;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by froran on 10/04/14.
 */
public class Person {
    private final int personId;
    private final String personName;

    public Person(ResultSet rs) throws SQLException {
        personId = rs.getInt("personid");
        personName = rs.getString("personname");
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
        buf.append("Person<person-id=")
            .append(personId)
            .append(", person-name=\"")
            .append(personName)
            .append("\"")
            .append(">");
        return buf.toString();
    }
}
