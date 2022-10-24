package dev.kosztadani.sandbox.rmi.server;

import dev.kosztadani.sandbox.rmi.common.Hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This is the main class of the RMI Server example application.
 */
public class ServerMain {

    private static final int REGISTRY_PORT = 1099;

    /**
     * The main method of the RMI server application.
     *
     * <p>
     * It creates a local registry on port 1099 and binds the implementation of {@link Hello}.
     *
     * @param args The command-line arguments passed to the application.
     *             These are ignored.
     */
    public static void main(final String[] args) {
        new ServerMain().run();
    }

    private void run() {
        try {
            doRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doRun() throws Exception {
        HelloService helloService = new HelloService();
        Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        registry.bind(Hello.REGISTRY_NAME, helloService);
    }
}
