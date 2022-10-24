package dev.kosztadani.sandbox.app;

/**
 * This is a sample application that prints "Hello, World!".
 */
public final class App {

    /**
     * The main method of the application.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(final String[] args) {
        new App().run();
    }

    private void run() {
        System.out.println("Hello, World!");
    }
}
