package dev.kosztadani.sandbox.tcp.client;

import dev.kosztadani.sandbox.tcp.common.io.SelectionKeyAttachment;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A single connection to a math server.
 */
final class MathClientChannel
    implements SelectionKeyAttachment, MathClient {

    private static final int BUFFER_SIZE = 1024;

    private final SelectionKey key;

    private final Selector selector;

    private final SocketChannel channel;

    private final ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private final ByteBuffer outputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private final Queue<MathTask> submittedTasks = new ConcurrentLinkedQueue<>();

    private final Queue<MathTask> bufferedTasks = new ConcurrentLinkedQueue<>();

    private volatile IOException terminationError = null;

    MathClientChannel(final SelectionKey key) {
        this.key = key;
        selector = key.selector();
        channel = (SocketChannel) key.channel();
    }

    @Override
    public void process() throws IOException {
        try {
            doProcess();
        } catch (IOException e) {
            terminate(e);
            throw e;
        }
    }

    private void doProcess() throws IOException {
        if (key.isConnectable()) {
            connect();
        }
        if (key.isReadable()) {
            read();
        }
        if (key.isWritable()) {
            write();
        }
    }

    private void connect() throws IOException {
        boolean connected = channel.finishConnect();
        if (connected) {
            key.interestOpsAnd(~SelectionKey.OP_CONNECT);
            key.interestOpsOr(SelectionKey.OP_READ);
            selector.wakeup();
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

    private void processInput() {
        while (inputBuffer.hasRemaining()) {
            inputBuffer.mark();
            try {
                processBuffer();
            } catch (BufferUnderflowException e) {
                inputBuffer.reset();
                break;
            }
        }
    }

    private void processBuffer() {
        MathResponse response = MathResponse.readFromBuffer(inputBuffer);
        MathTask task = bufferedTasks.remove();
        task.result().complete(response);
    }

    @Override
    public CompletableFuture<MathResponse> submit(final MathRequest request) {
        CompletableFuture<MathResponse> future = new CompletableFuture<>();
        MathTask task = new MathTask(request, future);
        submittedTasks.add(task);
        try {
            key.interestOpsOr(SelectionKey.OP_WRITE);
            selector.wakeup();
        } catch (CancelledKeyException e) {
            future.completeExceptionally(e);
        }
        if (terminationError != null) {
            future.completeExceptionally(terminationError);
        }
        return future;
    }

    private void write() throws IOException {
        moveTasksToBuffer();
        writeBufferToChannel();
    }

    private void moveTasksToBuffer() {
        while (!submittedTasks.isEmpty()) {
            outputBuffer.mark();
            try {
                moveTaskToBuffer();
            } catch (BufferOverflowException e) {
                outputBuffer.reset();
                break;
            }
        }
    }

    private void moveTaskToBuffer() {
        MathTask task = submittedTasks.element();
        task.request().writeToBuffer(outputBuffer);
        submittedTasks.remove();
        bufferedTasks.add(task);
    }

    private void writeBufferToChannel() throws IOException {
        outputBuffer.flip();
        channel.write(outputBuffer);
        if (!outputBuffer.hasRemaining() && submittedTasks.isEmpty()) {
            key.interestOpsAnd(~SelectionKey.OP_WRITE);
        }
        outputBuffer.compact();
    }

    private void terminate(final IOException reason) {
        terminationError = reason;
        for (MathTask task : submittedTasks) {
            task.result().completeExceptionally(reason);
        }
        for (MathTask task : bufferedTasks) {
            task.result().completeExceptionally(reason);
        }
    }

    private record MathTask(MathRequest request, CompletableFuture<MathResponse> result) {

    }
}
