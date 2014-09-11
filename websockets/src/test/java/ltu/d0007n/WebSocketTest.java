package ltu.d0007n;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.glassfish.tyrus.server.Server;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
            Map<String, Object> properties = new HashMap<>();

            Server server = new Server("localhost", 8081, "/test", properties, MediaServiceServerEndpoint.class);
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
