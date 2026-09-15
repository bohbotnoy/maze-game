package IO;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decompresses maze data that was compressed with MyCompressorOutputStream.
 * Unpacks bits back into individual bytes (0 or 1).
 */
public class MyDecompressorInputStream extends InputStream {

    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
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
     * Each compressed byte is unpacked into 8 individual cells (0 or 1).
     */
    @Override
    public int read(byte[] b) throws IOException {
        // Step 1: Read the first 12 bytes as-is (metadata)
        for (int i = 0; i < 12; i++) {
            b[i] = (byte) in.read();
        }

        // Step 2: Unpack the compressed maze data
        int index = 12;

        // Keep reading packed bytes until the output array is full
        while (index < b.length) {
            // Read one compressed byte (holds 8 maze cells)
            int packedByte = in.read();
            if (packedByte == -1) break; // safety check if stream ends early

            // Calculate how many bits to extract from this byte
            // Usually 8, but may be fewer if we're near the end of the array
            int bitsToExtract = Math.min(8, b.length - index);

            // Extract bits from left (bit 7) to right
            // Only extract the number of bits we actually need
            for (int bit = 7; bit >= 8 - bitsToExtract; bit--) {
                b[index] = (byte) ((packedByte >> bit) & 0x01);
                index++;
            }
        }

        // Read one last byte to consume the remainder indicator
        // that the compressor wrote at the end of the stream
        in.read();

        return index;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}