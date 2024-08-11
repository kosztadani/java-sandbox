package dev.kosztadani.sandbox.tcp.common.io;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;

/**
 * Marks objects intended to be attached to a {@link SelectionKey}.
 */
public interface SelectionKeyAttachment {

    /**
     * Executes the operations that the channel is ready to perform.
     *
     * @throws IOException           if an I/O exception occurs.
     * @throws CancelledKeyException if the {@link SelectionKey} has been cancelled.
     */
    void process() throws IOException;
}
