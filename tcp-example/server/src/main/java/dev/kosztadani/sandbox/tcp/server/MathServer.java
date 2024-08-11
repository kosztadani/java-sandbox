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
 * A server that listens on zero or more TCP sockets to serve clients.
 */
public final class MathServer
    implements Runnable, AutoCloseable {

    private final Selector selector;

    private volatile boolean closed = false;

    private MathServer(final Selector selector) {
        this.selector = selector;
    }

    /**
     * Creates, but doesn't start a new server. See {@link #run()}.
     *
     * @param bindAddresses zero or more addresses to bind to.
     * @return a new server.
     * @throws IOException if an I/O error occurs while creating the server.
     */
    public static MathServer create(final InetSocketAddress... bindAddresses)
        throws IOException {

        Selector selector = Selector.open();
        MathServer server = new MathServer(selector);
        try {
            for (InetSocketAddress address : bindAddresses) {
                server.listenOn(address);
            }
        } catch (IOException e) {
            try {
                selector.close();
            } catch (IOException closingException) {
                e.addSuppressed(closingException);
            }
            throw e;
        }
        return server;
    }

    /**
     * Adds an address for this server to listen on.
     *
     * @param serverAddress the new address to listen on.
     * @throws IOException if an I/O error occurs.
     */
    public void listenOn(final InetSocketAddress serverAddress) throws IOException {
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
        SelectionKeyAttachment attachment = new MathServerSocket(key);
        key.attach(attachment);
        key.interestOpsOr(SelectionKey.OP_ACCEPT);
        selector.wakeup();
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
