package ltu.d0007n;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collection;

/**
 * Unit tests for project.
 */
public class DemoTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DemoTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DemoTest.class );
    }

    /**
     * 
     */
    public void testDatabaseCreationAndInitiation()
    {
        try {
            Demo demo = new Demo();
            demo.create();
            demo.initiate();

            System.out.println("Database ready");
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
    public void testListStudents()
    {
        try {
            Demo demo = new Demo();
            Collection<String> students = demo.getAllStudents("D0007N");

            System.out.println("--- Students admitted to D0007N ---");
            for (String student : students) {
                System.out.println(student);
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
