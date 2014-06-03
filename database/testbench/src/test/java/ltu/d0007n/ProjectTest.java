package ltu.d0007n;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collection;

/**
 * Unit tests for project.
 */
public class ProjectTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ProjectTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ProjectTest.class );
    }

    /**
     * 
     */
    public void testDatabaseCreationAndInitiation()
    {
        try {
            EventController ec = new EventController();
            ec.create();
            ec.initiate();

            //
            ec.dumpDatabaseHistory();
            System.out.println();
        }
        catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();

            fail(info);
        }
    }

    /**
     *
     */
    public void testListEvents()
    {
        try {
            EventController ec = new EventController();
            Collection<Event> events = ec.getAllEvents();

            System.out.println("--- All events ---");
            for (Event event : events) {
                System.out.println(event);
            }
            System.out.println();
        }
        catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();

            fail(info);
        }
    }

    /**
     *
     */
    public void testListPersons()
    {
        try {
            EventController ec = new EventController();
            Collection<Person> persons = ec.getAllPersons();

            System.out.println("--- All persons ---");
            for (Person person : persons) {
                System.out.println(person);
            }
            System.out.println();
        }
        catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();

            fail(info);
        }
    }

    /**
     *
     */
    public void testListRegistrations()
    {
        try {
            EventController ec = new EventController();
            Collection<Registration> registrations = ec.getAllRegistrations();

            System.out.println("--- All registrations ---");
            for (Registration registration : registrations) {
                System.out.println(registration);
            }
            System.out.println();
        }
        catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();

            fail(info);
        }
    }

    /**
     *
     */
    public void testGetRegistrationsForPerson()
    {
        try {
            EventController ec = new EventController();
            Collection<Registration> registrations = ec.getAllRegistrationsFor("Frode Randers");

            System.out.println("--- Frodes registrations ---");
            for (Registration registration : registrations) {
                System.out.println(registration);
            }
            System.out.println();
        }
        catch (Exception e) {
            String info = "Failure: ";
            info += e.getMessage();
            System.err.println(info);
            e.printStackTrace();

            fail(info);
        }
    }


}
