package dev.kosztadani.sandbox.tcp.common.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * The supported mathematics operations.
 */
public enum MathOperationType
    implements Writeable {

    /**
     * Signed integer addition.
     */
    ADD((byte) 1),

    /**
     * Signed integer subtraction.
     */
    SUBTRACT((byte) 2),

    /**
     * Signed integer multiplication.
     */
    MULTIPLY((byte) 3),

    /**
     * Signed integer division.
     */
    DIVIDE((byte) 4);

    private final byte opcode;

    MathOperationType(final byte opcode) {
        this.opcode = opcode;
    }

    /**
     * Reads an operation type from the buffer.
     *
     * @param buffer The input buffer.
     * @return The operation, or an empty optional if the read data doesn't correspond to a supported operation type.
     * @throws BufferUnderflowException if the buffer doesn't have enough data.
     */
    public static Optional<MathOperationType> readFromBuffer(final ByteBuffer buffer) {
        byte b = buffer.get();
        for (MathOperationType operation : values()) {
            if (b == operation.opcode) {
                return Optional.of(operation);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the opcode of the operation.
     *
     * @return the opcode of the operation.
     */
    public byte opcode() {
        return opcode;
    }

    @Override
    public void writeToBuffer(final ByteBuffer buffer) {
        buffer.put(opcode);
    }
}
