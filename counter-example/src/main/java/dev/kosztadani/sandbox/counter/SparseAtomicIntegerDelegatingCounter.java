package dev.kosztadani.sandbox.counter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A counter implementation that delegates to a set of {@link AtomicInteger}s
 * laid out in an array such that (hopefully) most accesses by different
 * threads will not share the same cache line.
 */
public final class SparseAtomicIntegerDelegatingCounter
    extends AbstractSparseCounter {

    private final AtomicInteger[] counters;

    /**
     * Construct a new instance with default capacity and sparsity.
     */
    public SparseAtomicIntegerDelegatingCounter() {
        this(DEFAULT_CAPACITY, DEFAULT_SPARSITY);
    }

    /**
     * Construct a new instance with the given capacity.
     *
     * @param capacity The number of threads that will be able to increment the
     *                 counter concurrently without performance degradation due
     *                 to sharing cache lines.
     */
    public SparseAtomicIntegerDelegatingCounter(final int capacity) {
        this(capacity, DEFAULT_SPARSITY);
    }

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
    public SparseAtomicIntegerDelegatingCounter(final int capacity, final int sparsity) {
        super(capacity, sparsity);

        counters = new AtomicInteger[arraySize()];
        for (int i = 0; i < arraySize(); i++) {
            counters[i] = new AtomicInteger();
        }
    }

    @Override
    public int get() {
        int result = 0;
        synchronized (getLock()) {
            for (int i = 0; i < capacity(); i++) {
                int index = getIndex(i);
                result += counters[index].get();
            }
        }
        return result;
    }

    @Override
    public void increment() {
        int index = getIndexForThread();
        counters[index].incrementAndGet();
    }
}
