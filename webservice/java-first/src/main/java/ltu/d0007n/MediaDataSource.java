package ltu.d0007n;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An optimized data handler that does not cache (large) files in memory
 */
public class MediaDataSource implements javax.activation.DataSource {
    private String name;
    private String mimeType;
    private InputStream inputStream;

    MediaDataSource(String name, String mimeType, InputStream inputStream) {
        this.name = name;
        this.mimeType = mimeType;
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return mimeType;
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }
}
