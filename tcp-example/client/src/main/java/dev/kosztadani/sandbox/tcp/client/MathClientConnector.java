package dev.kosztadani.sandbox.tcp.client;

import dev.kosztadani.sandbox.tcp.common.io.SelectionKeyAttachment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Establishes connections (clients) to a math server.
 */
public final class MathClientConnector
    implements Runnable, AutoCloseable {

    private final Selector selector;

    private volatile boolean closed = false;

    private MathClientConnector(final Selector selector) {
        this.selector = selector;
    }

    /**
     * Creates, but doesn't start a new connector. See {@link #run()}.
     *
     * @return a new connector.
     * @throws IOException if an I/O error occurs while creating the client.
     */
    public static MathClientConnector create() throws IOException {
        Selector selector = Selector.open();
        MathClientConnector connector = new MathClientConnector(selector);
        return connector;
    }


    /**
     * Runs the server in the current thread.
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

    /**
     * Opens a new channel to a specific server address.
     *
     * @param serverAddress the address of the server.
     * @return the created client.
     * @throws IOException if an I/O error occurs.
     */
    public MathClient connect(final InetSocketAddress serverAddress) throws IOException {
        SocketChannel channel = SocketChannel.open();
        SelectionKey key;
        boolean connected;
        try {
            channel.configureBlocking(false);
            key = channel.register(selector, 0);
            connected = channel.connect(serverAddress);
        } catch (IOException e) {
            channel.close();
            throw e;
        }
        MathClientChannel client = new MathClientChannel(key);
        key.attach(client);
        if (connected) {
            key.interestOpsOr(SelectionKey.OP_READ);
        } else {
            key.interestOpsOr(SelectionKey.OP_CONNECT);
        }
        selector.wakeup();
        return client;
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
            } catch (IOException ee) {
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
