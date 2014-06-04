package ltu.d0007n;

import com.google.gson.Gson;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
//import org.glassfish.tyrus.core.websocket.ContainerProvider;
import org.glassfish.tyrus.server.Server;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Unit test for web service
 */
public class WebSocketTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WebSocketTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( WebSocketTest.class );
    }


    private Server createServer() {

        try {
            Server server = new Server("localhost", 8081, "/test", MediaServiceServerEndpoint.class);
            server.start();
            return server;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createClient() {

        String uri = "ws://localhost:8081/test/websocket/mediaservice";
        System.out.println("Connecting to embedded test-server through " + uri);

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(MediaServiceClientEndpoint.class, URI.create(uri));
        }
        catch (Throwable t) {
            String info = "Failed to connect to server: " + t.getMessage();
            System.out.println(info);
            t.printStackTrace();
        }
    }

    /**
     * Testing the MediaService web service
     */
    public void testWebSocket()
    {
        // Create service implementation, i.e. server
        Server server = createServer();

        // Create (test) client
        createClient();

        try {
            Thread.sleep(5 * 1000); // 5 seconds
        }
        catch (InterruptedException ie) {
            // Ignore
        }
        finally {
            server.stop();
        }
    }
}
