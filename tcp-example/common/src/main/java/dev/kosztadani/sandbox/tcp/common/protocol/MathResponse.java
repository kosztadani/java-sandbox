package dev.kosztadani.sandbox.tcp.common.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Represents a response to a {@link MathRequest}.
 *
 * @param result the result of the operation.
 */
public record MathResponse(int result)
    implements Writeable {

    /**
     * Reads a {@link MathResponse} from a buffer.
     *
     * @param buffer the input buffer.
     * @return the response.
     * @throws BufferUnderflowException if the buffer doesn't have enough data.
     */
    public static MathResponse readFromBuffer(final ByteBuffer buffer) {
        int result = buffer.getInt();
        return new MathResponse(result);
    }

    @Override
    public void writeToBuffer(final ByteBuffer buffer) {
        buffer.putInt(result);
    }
}
