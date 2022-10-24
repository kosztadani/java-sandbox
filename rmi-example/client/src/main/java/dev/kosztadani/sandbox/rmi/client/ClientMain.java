package dev.kosztadani.sandbox.rmi.client;

import dev.kosztadani.sandbox.rmi.common.Hello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This is the main class of the RMI Client example application.
 */
public final class ClientMain {

    /**
     * The main method of the RMI client application.
     *
     * <p>
     * It invokes the {@link Hello#hello(String)} method through RMI on the
     * server and prints the result on standard output.
     *
     * @param args The command-line arguments passed to the application.
     *             These are ignored.
     */
    public static void main(final String[] args) {
        new ClientMain().run();
    }

    private void run() {
        try {
            doRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doRun() throws Exception {
        Registry registry = LocateRegistry.getRegistry(null);
        Hello helloStub = (Hello) registry.lookup(Hello.REGISTRY_NAME);
        System.out.println(helloStub.hello("World"));
    }
}
