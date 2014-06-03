package ltu.d0007n;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * This is the media service definition
 */
@WebService(targetNamespace = "http://media.service.d0007n.ltu.se/")
public interface MediaService {

    // Get version of service
    String getVersion();

    // Get a specific media file (identified by it's ID)
    MediaFile getMediaFile(@WebParam(name="id") String id);
}
