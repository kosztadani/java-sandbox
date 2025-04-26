package dev.kosztadani.sandbox.tcp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;

/**
 * A math server.
 */
public sealed interface MathServer permits MathServerSocket {

    /**
     * Returns the socket address where the server is listening.
     *
     * @return the socket address where the server is listening.
     * @throws ClosedChannelException if the channel of the server is closed.
     * @throws IOException            if an I/O error occurs.
     */
    InetSocketAddress getAddress() throws IOException;
}
