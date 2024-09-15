package dev.kosztadani.sandbox.tcp.server;

import dev.kosztadani.sandbox.tcp.common.io.SelectionKeyAttachment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Allows binding to addresses to create {@link MathServer}s.
 */
public final class MathServerConnector
    implements Runnable, AutoCloseable {

    private final Selector selector;

    private volatile boolean closed = false;

    private MathServerConnector(final Selector selector) {
        this.selector = selector;
    }

    /**
     * Creates, but doesn't start a new server connector. See {@link #run()}.
     *
     * @return a new server connector.
     * @throws IOException if an I/O error occurs while creating the connector.
     */
    public static MathServerConnector create()
        throws IOException {

        Selector selector = Selector.open();
        MathServerConnector server = new MathServerConnector(selector);
        return server;
    }

    /**
     * Creates a server listening on a specified address.
     *
     * @param serverAddress the new address to listen on.
     * @return the server listening on the new address.
     * @throws IOException if an I/O error occurs.
     */
    public MathServer listenOn(final InetSocketAddress serverAddress) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        SelectionKey key;
        try {
            channel.configureBlocking(false);
            channel.bind(serverAddress);
            key = channel.register(selector, 0);
        } catch (IOException e) {
            try {
                channel.close();
            } catch (IOException closingException) {
                e.addSuppressed(closingException);
            }
            throw e;
        }
        MathServerSocket server = new MathServerSocket(key);
        key.attach(server);
        key.interestOpsOr(SelectionKey.OP_ACCEPT);
        selector.wakeup();
        return server;
    }

    /**
     * Runs the connector in the current thread.
     *
     * <p>
     * This blocks the thread. Usually, this should be executed in a separate thread.
     */
    @Override
    public void run() {
        while (!closed) {
            mainLoop();
        }
    }

    private void mainLoop() {
        try {
            selector.select(this::processKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClosedSelectorException e) {
            // close() was called right before select(), this is OK
        }
    }

    private void processKey(final SelectionKey key) {
        SelectionKeyAttachment attachment = (SelectionKeyAttachment) key.attachment();
        try {
            attachment.process();
        } catch (IOException e) {
            key.cancel();
            try {
                key.channel().close();
            } catch (IOException closingException) {
                // error while closing the channel, nothing to do
            }
        } catch (CancelledKeyException e) {
            // key is cancelled, this is fine
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
        selector.close();
    }
}
