package dev.kosztadani.sandbox.tcp.server;

import dev.kosztadani.sandbox.tcp.common.io.SelectionKeyAttachment;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Accepts incoming client connections and allocates a {@link MathServerChannel} for them.
 */
final class MathServerSocket
    implements SelectionKeyAttachment {

    private final SelectionKey key;

    private final Selector selector;

    private final ServerSocketChannel channel;

    MathServerSocket(final SelectionKey key) {
        this.key = key;
        selector = key.selector();
        channel = (ServerSocketChannel) key.channel();
    }

    @Override
    public void process() throws IOException {
        if (key.isAcceptable()) {
            accept();
        }
    }

    private void accept() throws IOException {
        SocketChannel clientChannel = channel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            SelectionKey clientKey = clientChannel.register(selector, 0);
            SelectionKeyAttachment attachment = new MathServerChannel(clientKey);
            clientKey.attach(attachment);
            clientKey.interestOpsOr(SelectionKey.OP_READ);
            selector.wakeup();
        }
    }
}
