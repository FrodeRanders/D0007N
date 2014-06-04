package ltu.d0007n;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by froran on 03/06/14.
 */
@ClientEndpoint
public class MediaServiceClientEndpoint {

    private Gson gson = new Gson();


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected ... " + session.getId() + " [" + (session.isOpen() ? "OPEN" : "CLOSE") + "]");

        //Thread t = new ServiceThread(session, "onOpen@client");
        //t.run();
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        if (null != message && message.length() > 0) {
            System.out.println("Client got: " + message);
        }
        return null;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(String.format("Client session %s close: %s", session.getId(), closeReason));
    }

    public class ServiceThread extends Thread {
        private final Session session;
        private final String requestedBy;

        public ServiceThread(Session session, final String requestedBy) {
            this.session = session;
            this.requestedBy = requestedBy;
        }

        public void run() {
            int i = 10;
            while (i-- > 0) {
                try {
                    Thread.sleep(1 * 1000);

                } catch (InterruptedException ie) {
                    // Ignore
                }

                System.out.println("Client sending on session " + session.getId() + " [" + (session.isOpen() ? "OPEN" : "CLOSED") + "]");

                if (session.isOpen()) {
                    MediaEvent msg = new MediaEvent("test", MediaServiceServerEndpoint.generateTestData(5, requestedBy));

                    if (false) {
                        session.getAsyncRemote().sendText(gson.toJson(msg));
                    } else {
                        try {
                            session.getBasicRemote().sendText(gson.toJson(msg));
                        } catch (IOException e) {
                            String info = "Could not send data to server for session " + session.getId();
                            System.out.println(info);
                        }
                    }
                }
            }
        }
    }
}