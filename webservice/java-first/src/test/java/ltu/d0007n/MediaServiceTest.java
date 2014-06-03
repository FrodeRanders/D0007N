package ltu.d0007n;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import javax.activation.DataHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for web service
 */
public class MediaServiceTest extends TestCase {

    private static final boolean EXTERNAL_SERVER = false;
    private static final String EXTERNAL_SERVER_URL = "http://localhost:8080";

    //
    private static boolean serverStarted = false;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MediaServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MediaServiceTest.class );
    }


    private void createServer() {
        if (serverStarted || EXTERNAL_SERVER)
            return;

        // Enable MTOM on server side
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("mtom-enabled", Boolean.TRUE);

        // Create service implementation, i.e. server
        MediaServiceImpl implementor = new MediaServiceImpl();

        JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setProperties(props);
        svrFactory.setServiceClass(MediaService.class);
        svrFactory.setAddress("http://localhost:9000/cxf/services/MediaService");
        svrFactory.setServiceBean(implementor);
        //svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        //svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        svrFactory.create();

        serverStarted = true;
    }

    private MediaService createClientJaxWsStyle() {
        // Enable MTOM on client side
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("mtom-enabled", Boolean.TRUE);

        // Create client
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setProperties(props);
        //factory.getOutInterceptors().add(new LoggingOutInterceptor());
        //factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.setServiceClass(MediaService.class);

        String serverURL;
        if (EXTERNAL_SERVER) {
            serverURL = EXTERNAL_SERVER_URL + "/cxf/services/MediaService";
            System.out.println("Connecting to external server at " + serverURL);
        } else {
            serverURL = "http://localhost:9000/cxf/services/MediaService";
            System.out.println("Connecting to embedded test-server through " + serverURL);
        }
        factory.setAddress(serverURL);
        MediaService client = (MediaService) factory.create();
        // ((BindingProvider)client).getRequestContext();
        return client;
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        // Create service implementation, i.e. server
        createServer();

        // Create (test) client
        MediaService client = createClientJaxWsStyle();

        String version = client.getVersion();
        System.out.println("Service version: " + version);

        MediaFile file = client.getMediaFile("42");
        DataHandler dh = file.fileData;
        try (InputStream is = dh.getInputStream()) {
            File outputFile = new File(file.fileName);
            if (outputFile.exists()) {
                outputFile.delete();
            }

            ltu.d0007n.internal.FileIO.writeToFile(is, outputFile);
        }
        catch (IOException ioe) {
            String info = "Could not retrieve media file: ";
            info += ioe.getMessage();

            System.err.println(info);
        }
    }
}
