package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Compresses maze data using simple run-length encoding.
 * The first 12 bytes (metadata) are written as-is.
 * From byte 12 onward, counts consecutive runs of 0s and 1s.
 */
public class SimpleCompressorOutputStream extends OutputStream {

    private OutputStream out;

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    /**
     * Writes a single byte to the output stream.
     * This is required by OutputStream but we don't use it directly.
     */
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    /**
     * Compresses the byte array and writes the result.
     * First 12 bytes are metadata (written as-is).
     * Remaining bytes are compressed using run-length encoding.
     */
    @Override
    public void write(byte[] b) throws IOException {
        // Step 1: Write the first 12 bytes as-is (metadata)
        for (int i = 0; i < 12; i++) {
            out.write(b[i] & 0xFF);
        }

        // Step 2: Compress maze data (from byte 12 onward)
        // We always start by counting 0s, then 1s, alternating
        int currentValue = 0; // start counting 0s
        int count = 0;

        for (int i = 12; i < b.length; i++) {
            int cellValue = b[i] & 0xFF;

            if (cellValue == currentValue) {
                // Same value continues the run
                count++;

                // If count reaches 255, write it and start a new run
                if (count == 255) {
                    out.write(255);
                    count = 0;
                    // Switch to the other value
                    currentValue = 1 - currentValue;
                    // Write 0 for the other value (it didn't appear)
                    out.write(0);
                    // Switch back to continue counting the same value
                    currentValue = 1 - currentValue;
                }
            } else {
                // Value changed - write the count and switch
                out.write(count);
                count = 1; // start counting the new value
                currentValue = 1 - currentValue;
            }
        }

        // Don't forget the last run!
        out.write(count);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}