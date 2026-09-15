package IO;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decompresses maze data that was compressed with SimpleCompressorOutputStream.
 * The first 12 bytes (metadata) are read as-is.
 * From byte 12 onward, reads run-length encoded data and expands it.
 */
public class SimpleDecompressorInputStream extends InputStream {

    private InputStream in;

    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * Reads a single byte from the input stream.
     * Required by InputStream.
     */
    @Override
    public int read() throws IOException {
        return in.read();
    }

    /**
     * Reads compressed data from the stream and fills byte array b
     * with the decompressed (expanded) maze data.
     */
    @Override
    public int read(byte[] b) throws IOException {
        // Step 1: Read the first 12 bytes as-is (metadata)
        for (int i = 0; i < 12; i++) {
            b[i] = (byte) in.read();
        }

        // Step 2: Decompress the maze data
        int currentValue = 0; // we start with 0s (same as compressor)
        int index = 12; // where to write in the output array

        int count;
        // Read counts one by one until the stream ends (-1)
        while ((count = in.read()) != -1 && index < b.length) {
            // Fill 'count' cells with the current value
            for (int i = 0; i < count && index < b.length; i++) {
                b[index] = (byte) currentValue;
                index++;
            }
            // Switch between 0 and 1
            currentValue = 1 - currentValue;
        }

        return index; // return number of bytes read
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}