package dev.kosztadani.sandbox.tcp.common.protocol;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * A protocol object that can be written to a {@link ByteBuffer}.
 */
interface Writeable {

    /**
     * Writes the object to the buffer.
     *
     * @param buffer the receiving buffer.
     * @throws BufferOverflowException if the buffer has overflowed. Part of the object may have already been written.
     */
    void writeToBuffer(ByteBuffer buffer);
}
