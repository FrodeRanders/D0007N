package ltu.d0007n.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by froran on 03/06/14.
 */
public class FileIO {

    /**
     * A nice one: http://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
     */
    public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            // prepare the buffer to be drained
            buffer.flip();
            // write to the channel, may block
            dest.write(buffer);
            // If partial transfer, shift remainder down
            // If buffer is empty, same as doing clear()
            buffer.compact();
        }
        // EOF will leave buffer in fill state
        buffer.flip();
        // make sure the buffer is fully drained.
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    /**
     * Writes from an InputStream to a file
     */
    public static File writeToFile(InputStream inputStream, File file) throws IOException {

        RandomAccessFile raf = null;
        try {
            // Wrap the input stream in some nice garment
            ReadableByteChannel inputChannel = Channels.newChannel(inputStream);

            // Wrap file in a file channel
            raf = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = raf.getChannel();

            // Transfer from input stream to file
            fastChannelCopy(inputChannel, fileChannel);

        } finally {
            // Close resources and such
            if (null != raf) raf.close(); // closes fileChannel as well
        }

        return file;
    }

}
