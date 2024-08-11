package dev.kosztadani.sandbox.tcp.client;

import dev.kosztadani.sandbox.tcp.common.protocol.MathOperationType;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.threading.ThreadFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * Entry point to the TCP client example application.
 */
public final class MathClientMain {

    private static final String THREAD_NAME = "math-client";

    private static final MathRequest CONNECTION_TEST = new MathRequest(MathOperationType.ADD, 0, 0);

    private final String[] args;

    private MathClientMain(final String[] args) {
        this.args = args;
    }

    /**
     * The main method of the TCP client application.
     *
     * @param args The command-line arguments passed to the application.
     * @throws Exception if an error occurs.
     */
    public static void main(final String[] args) throws Exception {
        new MathClientMain(args).run();
    }

    private void run() throws IOException {
        Optional<InetSocketAddress> serverAddress = parseAddress();
        if (serverAddress.isPresent()) {
            connect(serverAddress.get());
        } else {
            printHelpAndExit();
        }
    }

    private Optional<InetSocketAddress> parseAddress() {
        try {
            InetAddress address = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            return Optional.of(new InetSocketAddress(address, port));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void connect(final InetSocketAddress address) throws IOException {
        try (MathClientConnector connector = MathClientConnector.create()) {
            ThreadFactory.newCriticalThread(connector, THREAD_NAME).start();
            MathClient client = connector.connect(address);
            // test the connection
            client.request(CONNECTION_TEST);
            // process user input until terminated
            new MathClientCli(client, System.in).run();
        }
    }

    private void printHelpAndExit() {
        System.err.println("Usage: client <address> <port>");
        System.exit(1);
    }
}
