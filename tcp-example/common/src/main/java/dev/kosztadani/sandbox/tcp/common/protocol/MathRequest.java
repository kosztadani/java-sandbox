package dev.kosztadani.sandbox.tcp.common.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * A request to perform a mathematics operation.
 *
 * @param operation The operation.
 * @param left      The left operand.
 * @param right     The right operand.
 */
public record MathRequest(MathOperationType operation, int left, int right)
    implements Writeable {

    /**
     * Reads a {@link MathRequest} from a buffer.
     *
     * @param buffer the input buffer.
     * @return The request, or an empty optional if it is not a valid request.
     * @throws BufferUnderflowException if the buffer doesn't have enough data.
     *                                  Part of the object may have already been read.
     */
    public static Optional<MathRequest> readFromBuffer(final ByteBuffer buffer) {
        Optional<MathOperationType> operation = MathOperationType.readFromBuffer(buffer);
        int left = buffer.getInt();
        int right = buffer.getInt();
        return operation.map(op -> new MathRequest(op, left, right));
    }

    @Override
    public void writeToBuffer(final ByteBuffer buffer) {
        operation.writeToBuffer(buffer);
        buffer.putInt(left);
        buffer.putInt(right);
    }
}
