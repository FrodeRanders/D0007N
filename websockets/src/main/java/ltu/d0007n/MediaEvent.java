package ltu.d0007n;

import java.util.Collection;

/**
 * Created by froran on 04/06/14.
 */
public class MediaEvent {
    String event;
    Collection<MediaData> data;

    public MediaEvent(String event, Collection<MediaData> data) {
        this.event = event;
        this.data = data;
    }
}
