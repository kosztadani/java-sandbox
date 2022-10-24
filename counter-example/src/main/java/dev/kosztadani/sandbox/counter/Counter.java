package dev.kosztadani.sandbox.counter;

/**
 * Thread-safe counter.
 *
 * <p>
 * Implementations must be thread-safe.
 */
public interface Counter {

    /**
     * Gets the current value of the counter.
     *
     * @return The current value of the counter.
     */
    int get();

    /**
     * Increments the counter.
     */
    void increment();

    /**
     * Increments the counter and gets the value after the increment.
     *
     * @return The current value of the counter after incrementing it.
     */
    int incrementAndGet();

}
