package dev.kosztadani.sandbox.counter;

import java.util.concurrent.atomic.AtomicInteger;

abstract class AbstractSparseCounter
    implements Counter {

    // One data point for every 32 integers (i.e. 32 * 4 = 128 bytes).
    static final int DEFAULT_SPARSITY = 32;

    static final int DEFAULT_CAPACITY = 16;

    private final int capacity;

    private final int sparsity;

    private final Object lock = new Object();

    private final AtomicInteger threadCounter = new AtomicInteger();

    private final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(threadCounter::incrementAndGet);

    /**
     * Construct a new instance with the given capacity and sparsity.
     *
     * @param capacity The number of threads that will be able to increment the
     *                 counter concurrently without performance degradation due
     *                 to sharing cache lines.
     * @param sparsity The padding factor for the array where the counters are
     *                 stored. E.g., a value of 32 means that only every 32nd
     *                 element is used as a data store, the rest is padding.
     */
    AbstractSparseCounter(final int capacity, final int sparsity) {
        this.capacity = capacity;
        this.sparsity = sparsity;
    }

    public abstract int get();

    public abstract void increment();

    @Override
    public int incrementAndGet() {
        synchronized (getLock()) {
            increment();
            return get();
        }
    }

    int capacity() {
        return capacity;
    }

    Object getLock() {
        return lock;
    }

    int arraySize() {
        // the first slot is left empty, to provide some padding there too
        return (capacity + 1) * sparsity;
    }

    int getIndexForThread() {
        return getIndex(threadId.get() % capacity);
    }

    int getIndex(final int slot) {
        return (slot + 1) * sparsity;
    }
}
