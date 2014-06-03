package ltu.d0007n;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implements the media service
 */
@WebService(targetNamespace = "http://media.service.d0007n.ltu.se/",
            endpointInterface = "ltu.d0007n.MediaService")
public class MediaServiceImpl implements MediaService {

    public String getVersion() {
        return "1.0";
    }

    public MediaFile getMediaFile(String id) {
        String fileName = "You_have_been_RickRolled.mp4"; // Yikes!
        String mimeType = "video/mp4";

        // Don't close this input stream!!!
        InputStream is = getClass().getResourceAsStream("binary.data");

        DataSource ds = new MediaDataSource(fileName, mimeType, is);
        DataHandler dh = new DataHandler(ds);

        MediaFile file = new MediaFile();
        file.fileName = fileName;
        file.fileData = dh;

        return file;
    }
}
