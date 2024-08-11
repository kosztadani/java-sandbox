package dev.kosztadani.sandbox.tcp.common.threading;

/**
 * Creates {@link Thread}s.
 */
public final class ThreadFactory {

    private ThreadFactory() {
    }

    /**
     * Creates a new thread, whose abrupt termination crashes the application.
     *
     * @param task the task which will be executed in the thread.
     * @param name the name of the thread.
     * @return the new thread.
     */
    public static Thread newCriticalThread(final Runnable task, final String name) {
        Thread newThread = new Thread(task, name);
        newThread.setUncaughtExceptionHandler(ThreadFactory::crashFromException);
        return newThread;
    }

    private static void crashFromException(final Thread thread, final Throwable throwable) {
        System.err.println("Unhandled exception in thread " + thread + ":");
        throwable.printStackTrace();
        System.exit(1);
    }
}
