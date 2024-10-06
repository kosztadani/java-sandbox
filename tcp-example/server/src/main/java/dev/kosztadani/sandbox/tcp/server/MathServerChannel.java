package dev.kosztadani.sandbox.tcp.server;

import dev.kosztadani.sandbox.tcp.common.io.SelectionKeyAttachment;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Optional;

/**
 * Handles a single client's interaction with a {@link MathServer}.
 */
final class MathServerChannel
    implements SelectionKeyAttachment {

    private static final int BUFFER_SIZE = 1024 * 1024;

    private final SelectionKey key;

    private final Selector selector;

    private final SocketChannel channel;

    private final ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private final ByteBuffer outputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    /**
     * Creates a channel for a client.
     *
     * @param key The {@link SelectionKey} to which this object will be attached.
     */
    MathServerChannel(final SelectionKey key) {
        this.key = key;
        selector = key.selector();
        channel = (SocketChannel) key.channel();
    }

    @Override
    public void process() throws IOException {
        if (key.isReadable()) {
            read();
        }
        if (key.isWritable()) {
            write();
        }
    }

    private void read() throws IOException {
        int bytesRead = channel.read(inputBuffer);
        if (bytesRead == -1) {
            throw new IOException("End of stream reached.");
        }
        inputBuffer.flip();
        processInput();
        inputBuffer.compact();
    }

    private void processInput() throws IOException {
        while (inputBuffer.hasRemaining()) {
            inputBuffer.mark();
            try {
                processBuffer();
            } catch (BufferUnderflowException e) {
                inputBuffer.reset();
                break;
            }
        }
        if (outputBuffer.position() != 0) {
            key.interestOpsOr(SelectionKey.OP_WRITE);
            selector.wakeup();
        }
    }

    private void processBuffer() throws IOException {
        Optional<MathRequest> request = MathRequest.readFromBuffer(inputBuffer);
        Optional<MathResponse> response = request.map(this::processRequest);
        if (response.isPresent()) {
            writeResponse(response.get());
        }
    }

    private MathResponse processRequest(final MathRequest request) {
        int result = switch (request.operation()) {
            case ADD -> request.left() + request.right();
            case SUBTRACT -> request.left() - request.right();
            case MULTIPLY -> request.left() * request.right();
            case DIVIDE -> request.left() / request.right();
        };
        return new MathResponse(result);
    }

    private void writeResponse(final MathResponse response) throws IOException {
        try {
            response.writeToBuffer(outputBuffer);
        } catch (BufferOverflowException e) {
            key.cancel();
            channel.close();
        }
    }

    private void write() throws IOException {
        outputBuffer.flip();
        channel.write(outputBuffer);
        if (!outputBuffer.hasRemaining()) {
            key.interestOpsAnd(~SelectionKey.OP_WRITE);
        }
        outputBuffer.compact();
    }
}
