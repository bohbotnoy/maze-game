package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Compresses maze data using bit packing.
 * Each 8 maze cells (0 or 1) are packed into a single byte.
 */
public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        // Step 1: Write the first 12 bytes as-is (metadata)
        for (int i = 0; i < 12; i++) {
            out.write(b[i] & 0xFF);
        }

        // Step 2: Pack every 8 maze cells into 1 byte
        int mazeLength = b.length - 12;
        int i = 12;

        while (i < b.length) {
            int packedByte = 0;
            int bitsInThisByte = 0;

            for (int bit = 0; bit < 8 && i < b.length; bit++) {
                packedByte = (packedByte << 1) | (b[i] & 0x01);
                bitsInThisByte++;
                i++;
            }

            // If last group has fewer than 8 bits, pad with zeros on the right
            if (bitsInThisByte < 8) {
                packedByte = packedByte << (8 - bitsInThisByte);
            }

            out.write(packedByte);
        }

        // Write how many valid bits are in the last byte (1-8)
        int remainder = mazeLength % 8;
        if (remainder == 0) remainder = 8;
        out.write(remainder);
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