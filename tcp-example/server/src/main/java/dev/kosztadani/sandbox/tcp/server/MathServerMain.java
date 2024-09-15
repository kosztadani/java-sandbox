package dev.kosztadani.sandbox.tcp.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * Entry point to the TCP server example application.
 */
public final class MathServerMain {

    private final String[] args;

    private MathServerMain(final String[] args) {
        this.args = args;
    }

    /**
     * The main method of the TCP server application.
     *
     * @param args The command-line arguments passed to the application.
     * @throws Exception if an error occurs.
     */
    public static void main(final String[] args) throws Exception {
        new MathServerMain(args).run();
    }

    private void run() throws IOException {
        Optional<InetSocketAddress> serverAddress = getAddress();
        if (serverAddress.isPresent()) {
            try (MathServerConnector serverConnector = MathServerConnector.create()) {
                MathServer server = serverConnector.listenOn(serverAddress.get());
                int port = server.getAddress().getPort();
                System.out.println("Listening on port: " + port);
                serverConnector.run();
            }
        } else {
            printHelpAndExit();
        }
    }

    private Optional<InetSocketAddress> getAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            int port = Integer.parseInt(args[0]);
            InetSocketAddress address = new InetSocketAddress(localhost, port);
            return Optional.of(address);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void printHelpAndExit() {
        System.err.println("Usage: server <port>");
        System.exit(1);
    }
}
