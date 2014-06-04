package ltu.d0007n;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by froran on 03/06/14.
 */
@ServerEndpoint(value = "/websocket/mediaservice")
public class MediaServiceServerEndpoint {

    private static int testId = 0;

    private Gson gson = new Gson();

    public static Collection<MediaData> generateTestData(int count, final String requestedBy) {
        System.out.print("Generating testdata: from=" + (testId + 1));
        Collection<MediaData> data = new ArrayList<>();
        while (count-- > 0) {
            data.add(new MediaData("" + (++testId), "http://localhost:8080/websocket-example/test/" + testId));
        }
        System.out.println(" to=" + testId + " requested-by=" + requestedBy);
        return data;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Server connected ... " + session.getId() + " [" + (session.isOpen() ? "OPEN" : "CLOSE") + "]");

        Thread t = new ServiceThread(session, "onOpen@server");
        t.run();
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        if (null != message && message.length() > 0) {
            System.out.println("Server got: " + message);
        }
        return null;
    }

    @OnMessage
    public void onBinaryMessage(Session session, ByteBuffer bb, boolean last) {
        System.out.println("Server got binary msg from " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Server session %s closed: %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String info = "Error communicating with " + session.getId();
        info += ": " + throwable.getMessage();
        System.out.println(info);
        //throwable.printStackTrace();
    }


    public class ServiceThread extends Thread {
        private final Session session;
        private final String requestedBy;

        public ServiceThread(Session session, final String requestedBy) {
            this.session = session;
            this.requestedBy = requestedBy;
        }

        public void run() {
            int i = 0;
            while (i++ < 10) {
                try {
                    Thread.sleep(2 * 1000);

                } catch (InterruptedException ie) {
                    // Ignore
                }
                System.out.println("Server sending on session " + session.getId() + " [" + (session.isOpen() ? "OPEN" : "CLOSED") + "]");

                if (session.isOpen()) {
                    MediaEvent msg = new MediaEvent("list_update", generateTestData(i, requestedBy));

                    if (false) {
                        session.getAsyncRemote().sendText(gson.toJson(msg));
                    } else {
                        try {
                            session.getBasicRemote().sendText(gson.toJson(msg));
                        } catch (IOException e) {
                            String info = "Could not send data to client for session " + session.getId();
                            System.out.println(info);
                        }
                    }
                }
            }
        }
    }

}
